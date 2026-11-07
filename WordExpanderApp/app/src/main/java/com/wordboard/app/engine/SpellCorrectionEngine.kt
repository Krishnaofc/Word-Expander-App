package com.wordboard.app.engine

import kotlin.math.min

/**
 * Intelligent Typo & Misspelling Correction Engine.
 * Holds an extensive dictionary of 70+ common typos across English, Hindi & Hinglish,
 * and performs Levenshtein distance matching on user shortcuts.
 */
class SpellCorrectionEngine {

    // Comprehensive dictionary of common typing mistakes and corrections
    private val commonTypos = mapOf(
        // English frequently misspelled words
        "teh" to "the",
        "recieve" to "receive",
        "recieved" to "received",
        "recieving" to "receiving",
        "seperate" to "separate",
        "seperated" to "separated",
        "becuase" to "because",
        "definatly" to "definitely",
        "definately" to "definitely",
        "untill" to "until",
        "wich" to "which",
        "adn" to "and",
        "dont" to "don't",
        "wont" to "won't",
        "cant" to "can't",
        "didnt" to "didn't",
        "doesnt" to "doesn't",
        "isnt" to "isn't",
        "alot" to "a lot",
        "occured" to "occurred",
        "occuring" to "occurring",
        "truely" to "truly",
        "goverment" to "government",
        "acess" to "access",
        "sucessful" to "successful",
        "sucess" to "success",
        "tmmrw" to "tomorrow",
        "tommorrow" to "tomorrow",
        "tomorow" to "tomorrow",
        "calender" to "calendar",
        "wierd" to "weird",
        "neccessary" to "necessary",
        "beleive" to "believe",
        "acheive" to "achieve",
        "embarass" to "embarrass",
        "enviroment" to "environment",
        "mispell" to "misspell",
        "peice" to "piece",
        "pronounciation" to "pronunciation",
        "recommand" to "recommend",
        "reccomend" to "recommend",
        "tommorow" to "tomorrow",
        "weather" to "whether",

        // Hindi / Hinglish transliteration mistakes & standard corrections
        "nmaste" to "नमस्ते",
        "namste" to "नमस्ते",
        "nmste" to "नमस्ते",
        "namaskar" to "नमस्कार",
        "nmskar" to "नमस्कार",
        "dhnyawad" to "धन्यवाद",
        "dhanywad" to "धन्यवाद",
        "dhanyavaad" to "धन्यवाद",
        "dhnwad" to "धन्यवाद",
        "shukrya" to "शुक्रिया",
        "sukriya" to "शुक्रिया",
        "shukria" to "शुक्रिया",
        "sukria" to "शुक्रिया",
        "apka" to "आपका",
        "aapka" to "आपका",
        "kese" to "कैसे",
        "kaise" to "कैसे",
        "alwida" to "अलविदा",
        "badhai" to "बधाई",
        "badhaiyan" to "बधाइयाँ",
        "shubhkamna" to "शुभकामनाएं",
        "subhkamna" to "शुभकामनाएं",
        "kripya" to "कृपया",
        "kirpya" to "कृपया",
        "krpya" to "कृपया",
        "swagat" to "स्वागत",
        "suwagat" to "स्वागत",
        "dhanyawad" to "धन्यवाद",
        "prnam" to "प्रणाम",
        "pranaam" to "प्रणाम",
        "madad" to "मदद",
        "zaroor" to "ज़रूर",
        "jarur" to "ज़रूर",
        "alvida" to "अलविदा",
        "khayal" to "ख़्याल"
    )

    /**
     * Checks if the word is in the common typo dictionary.
     */
    fun findCorrectionForTypo(word: String): String? {
        val lower = word.lowercase().trim()
        return commonTypos[lower]
    }

    /**
     * Calculates Levenshtein distance between two strings (Unicode safe).
     */
    fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }

        for (i in 0..s1.length) {
            dp[i][0] = i
        }
        for (j in 0..s2.length) {
            dp[0][j] = j
        }

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = min(
                    dp[i - 1][j] + 1, // deletion
                    min(
                        dp[i][j - 1] + 1, // insertion
                        dp[i - 1][j - 1] + cost // substitution
                    )
                )
            }
        }
        return dp[s1.length][s2.length]
    }

    /**
     * Finds the closest shortcut if the user made a minor typing mistake (distance <= 1 or 2).
     */
    fun findClosestShortcut(input: String, registeredShortcuts: List<String>): String? {
        if (input.length < 3) return null

        var bestMatch: String? = null
        var minDistance = Int.MAX_VALUE

        for (shortcut in registeredShortcuts) {
            val dist = levenshteinDistance(input.lowercase(), shortcut.lowercase())
            // Allow distance of 1 for words length 3-5, distance of 2 for length > 5
            val maxAllowedDist = if (shortcut.length <= 4) 1 else 2
            if (dist in 1..maxAllowedDist && dist < minDistance) {
                minDistance = dist
                bestMatch = shortcut
            }
        }
        return bestMatch
    }
}
