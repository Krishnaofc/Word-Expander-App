package com.wordboard.app.service

import android.accessibilityservice.AccessibilityService
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.wordboard.app.data.AppDatabase
import com.wordboard.app.data.SnippetDao
import com.wordboard.app.data.SnippetEntity
import com.wordboard.app.engine.SpellCorrectionEngine
import com.wordboard.app.engine.UniversalExpanderEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Global Accessibility Service for WordBoard.
 * Listens to text changes across all apps, detects shortcuts in ANY language/script,
 * and seamlessly provides 1-tap floating popup expansion or automatic expansion.
 */
class WordBoardAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var snippetDao: SnippetDao
    private lateinit var expanderEngine: UniversalExpanderEngine
    private lateinit var spellCorrectionEngine: SpellCorrectionEngine
    private lateinit var popupManager: OverlayPopupManager

    // Prevent recursive loop when this service modifies text
    private var isUpdatingText = false

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getDatabase(this)
        snippetDao = db.snippetDao()
        expanderEngine = UniversalExpanderEngine(this)
        spellCorrectionEngine = SpellCorrectionEngine()
        popupManager = OverlayPopupManager(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || isUpdatingText) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                handleTextChanged(event)
            }
            AccessibilityEvent.TYPE_VIEW_FOCUSED,
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                // Dismiss popup when user switches fields or apps
                popupManager.dismissPopup()
            }
        }
    }

    private fun handleTextChanged(event: AccessibilityEvent) {
        val source = event.source ?: return
        val currentText = source.text?.toString() ?: ""

        val candidate = expanderEngine.extractLastWord(currentText) ?: run {
            popupManager.dismissPopup()
            return
        }

        val typedWord = candidate.word
        if (typedWord.isBlank()) return

        serviceScope.launch {
            // 1. Check if typedWord matches a registered shortcut
            val snippet = snippetDao.findByShortcut(typedWord)
            if (snippet != null) {
                handleSnippetMatch(source, candidate.startIndex, candidate.endIndex, typedWord, snippet)
                return@launch
            }

            // 2. Check if typedWord is a common typo or close to a known shortcut
            val typoCorrection = spellCorrectionEngine.findCorrectionForTypo(typedWord)
            if (typoCorrection != null) {
                withContext(Dispatchers.Main) {
                    popupManager.showExpansionPopup(
                        shortcut = typedWord,
                        previewText = typoCorrection,
                        isCorrection = true
                    ) {
                        applyReplacement(source, candidate.startIndex, candidate.endIndex, typoCorrection)
                    }
                }
                return@launch
            }

            // 3. Check fuzzy distance to known shortcuts
            val allSnippets = snippetDao.getAllSnippetsList()
            val shortcutList = allSnippets.map { it.shortcut }
            val closestShortcut = spellCorrectionEngine.findClosestShortcut(typedWord, shortcutList)

            if (closestShortcut != null) {
                val matchedSnippet = allSnippets.firstOrNull { it.shortcut == closestShortcut }
                if (matchedSnippet != null) {
                    val resolved = expanderEngine.resolveTemplate(matchedSnippet.expandedText)
                    withContext(Dispatchers.Main) {
                        popupManager.showExpansionPopup(
                            shortcut = "$typedWord -> ${matchedSnippet.shortcut}",
                            previewText = resolved.expandedText,
                            isCorrection = true
                        ) {
                            applyReplacement(source, candidate.startIndex, candidate.endIndex, resolved.expandedText)
                            serviceScope.launch { snippetDao.recordUsage(matchedSnippet.id) }
                        }
                    }
                }
            } else {
                // No match, dismiss popup
                withContext(Dispatchers.Main) {
                    popupManager.dismissPopup()
                }
            }
        }
    }

    private suspend fun handleSnippetMatch(
        source: AccessibilityNodeInfo,
        startIndex: Int,
        endIndex: Int,
        typedShortcut: String,
        snippet: SnippetEntity
    ) {
        val resolved = expanderEngine.resolveTemplate(snippet.expandedText)
        val finalText = if (!snippet.isCaseSensitive) {
            expanderEngine.matchCase(typedShortcut, resolved.expandedText)
        } else {
            resolved.expandedText
        }

        if (snippet.triggerMode == "AUTO") {
            // Immediate auto-expansion
            applyReplacement(source, startIndex, endIndex, finalText)
            snippetDao.recordUsage(snippet.id)
        } else {
            // Show interactive floating pill above the keyboard
            withContext(Dispatchers.Main) {
                popupManager.showExpansionPopup(
                    shortcut = snippet.shortcut,
                    previewText = finalText,
                    isCorrection = false
                ) {
                    applyReplacement(source, startIndex, endIndex, finalText)
                    serviceScope.launch { snippetDao.recordUsage(snippet.id) }
                }
            }
        }
    }

    /**
     * Replaces the typed shortcut range with the expanded text in the target node.
     */
    private fun applyReplacement(
        node: AccessibilityNodeInfo,
        start: Int,
        end: Int,
        replacement: String
    ) {
        try {
            isUpdatingText = true
            val fullText = node.text?.toString() ?: ""
            val newText = StringBuilder(fullText)
                .replace(start, end, replacement)
                .toString()

            val arguments = Bundle().apply {
                putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, newText)
            }
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)

            // Vibrate haptic confirmation
            triggerHapticFeedback()
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback via clipboard if ACTION_SET_TEXT is restricted by host app
            copyToClipboard(replacement)
        } finally {
            isUpdatingText = false
        }
    }

    private fun triggerHapticFeedback() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(30)
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("WordBoard", text)
        clipboard?.setPrimaryClip(clip)
    }

    override fun onInterrupt() {
        popupManager.dismissPopup()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        popupManager.dismissPopup()
    }
}
