package com.wordboard.app.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a text expansion snippet supporting all world languages and scripts (UTF-8).
 */
@Entity(
    tableName = "snippets",
    indices = [
        Index(value = ["shortcut"], unique = true),
        Index(value = ["category"]),
        Index(value = ["languageCode"])
    ]
)
data class SnippetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /**
     * The trigger prefix or shortcut word.
     * Can be in ANY script or language (e.g. "omw", "nmt", "/addr", "नमस्ते", "شكرا", "おはよう").
     */
    val shortcut: String,

    /**
     * The full expanded text or template.
     * Supports dynamic tokens:
     * - {date}: Current formatted date
     * - {time}: Current formatted time
     * - {clipboard}: Pastes current clipboard contents
     * - {cursor}: Places cursor at this spot
     */
    val expandedText: String,

    /**
     * Friendly label/title for user display.
     */
    val label: String = "",

    /**
     * Category for organization (e.g., "General", "Greetings", "Business", "Email", "Hindi", "Social").
     */
    val category: String = "General",

    /**
     * ISO 639-1 language code or "any" for universal.
     */
    val languageCode: String = "any",

    /**
     * If true, "OMW" will match only "OMW". If false, "omw" matches "Omw" or "OMW" (preserving case).
     */
    val isCaseSensitive: Boolean = false,

    /**
     * Expansion behavior:
     * - "POPUP": Shows a sleek floating pill above the keyboard asking user to tap to expand.
     * - "AUTO": Automatically replaces the shortcut immediately when space or delimiter is typed.
     * - "BOTH": Auto-expands on space, but shows popup while typing.
     */
    val triggerMode: String = "POPUP",

    /**
     * Usage statistics to prioritize frequent phrases.
     */
    val usageCount: Int = 0,
    val lastUsedTimestamp: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)
