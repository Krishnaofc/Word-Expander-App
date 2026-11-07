package com.wordboard.app.engine

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Universal Text Expander Engine supporting ALL languages and character sets (UTF-8).
 * Handles Unicode normalization, token replacement, case matching, and delimiter extraction.
 */
class UniversalExpanderEngine(private val context: Context) {

    /**
     * Resolves dynamic template tokens:
     * - {date}: Locale formatted date
     * - {time}: Locale formatted time
     * - {clipboard}: Pastes current clipboard text safely
     * - {cursor}: Marks the cursor stopping point
     */
    fun resolveTemplate(template: String, locale: Locale = Locale.getDefault()): ExpansionResult {
        var result = template

        // 1. {date}
        if (result.contains("{date}")) {
            val dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, locale)
            val currentDate = dateFormat.format(Date())
            result = result.replace("{date}", currentDate)
        }

        // 2. {time}
        if (result.contains("{time}")) {
            val timeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, locale)
            val currentTime = timeFormat.format(Date())
            result = result.replace("{time}", currentTime)
        }

        // 3. {clipboard}
        if (result.contains("{clipboard}")) {
            val clipboardText = getClipboardText()
            result = result.replace("{clipboard}", clipboardText)
        }

        // 4. {cursor}
        var cursorOffset = -1
        if (result.contains("{cursor}")) {
            cursorOffset = result.indexOf("{cursor}")
            result = result.replace("{cursor}", "")
        }

        return ExpansionResult(
            expandedText = result,
            cursorOffset = if (cursorOffset >= 0) cursorOffset else result.length
        )
    }

    /**
     * Adjusts the casing of the expanded text to match the input shortcut casing:
     * - All lowercase: omw -> on my way
     * - Capitalized:   Omw -> On my way
     * - All uppercase: OMW -> ON MY WAY
     */
    fun matchCase(originalInput: String, expandedText: String): String {
        if (originalInput.isBlank() || expandedText.isBlank()) return expandedText

        return when {
            isAllUpperCase(originalInput) -> expandedText.uppercase(Locale.getDefault())
            isCapitalized(originalInput) -> {
                val firstChar = expandedText.first().uppercase()
                firstChar + expandedText.substring(1)
            }
            else -> expandedText
        }
    }

    private fun isAllUpperCase(str: String): Boolean {
        val letters = str.filter { it.isLetter() }
        return letters.isNotEmpty() && letters.all { it.isUpperCase() }
    }

    private fun isCapitalized(str: String): Boolean {
        val letters = str.filter { it.isLetter() }
        return letters.isNotEmpty() && letters.first().isUpperCase() && letters.drop(1).all { it.isLowerCase() }
    }

    /**
     * Extracts the trailing candidate word / shortcut from a full string of text.
     * Works with all world languages including non-Latin scripts (Hindi, Arabic, Chinese, etc.).
     */
    fun extractLastWord(text: CharSequence?): WordCandidate? {
        if (text.isNullOrEmpty()) return null

        val normalized = Normalizer.normalize(text, Normalizer.Form.NFC)
        val len = normalized.length

        // Find the last delimiter (space, newline, comma, tab, punctuation)
        var i = len - 1
        while (i >= 0) {
            val c = normalized[i]
            if (isDelimiter(c)) {
                break
            }
            i--
        }

        val startIndex = i + 1
        if (startIndex < len) {
            val candidateWord = normalized.substring(startIndex, len)
            return WordCandidate(
                word = candidateWord,
                startIndex = startIndex,
                endIndex = len
            )
        }
        return null
    }

    /**
     * Checks if a character serves as a natural word delimiter across world languages.
     */
    fun isDelimiter(c: Char): Boolean {
        return c.isWhitespace() ||
                c == '\n' || c == '\r' || c == '\t' ||
                c == ',' || c == '.' || c == '?' || c == '!' ||
                c == ';' || c == ':' || c == '(' || c == ')' ||
                c == '[' || c == ']' || c == '{' || c == '}' ||
                c == '،' || c == '؛' || c == '؟' || // Arabic punctuation
                c == '।' || c == '॥' ||             // Devanagari purna viram
                c == '。' || c == '、' || c == '！' || c == '？' // CJK punctuation
    }

    private fun getClipboardText(): String {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = clipboard?.primaryClip
            if (clip != null && clip.itemCount > 0) {
                clip.getItemAt(0).coerceToText(context).toString()
            } else ""
        } catch (e: Exception) {
            ""
        }
    }
}

data class WordCandidate(
    val word: String,
    val startIndex: Int,
    val endIndex: Int
)

data class ExpansionResult(
    val expandedText: String,
    val cursorOffset: Int
)
