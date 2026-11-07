package com.wordboard.app.service

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import com.wordboard.app.R
import com.wordboard.app.data.AppDatabase
import com.wordboard.app.data.SnippetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * WordBoard Custom Keyboard Input Method Service (IME).
 * Allows users to set WordBoard as an active keyboard or switch to it
 * for a dedicated phrase board & shortcut launcher.
 */
class WordBoardIMEKeyboardService : InputMethodService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var snippetsContainer: LinearLayout? = null

    override fun onCreateInputView(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0xFF0F172A.toInt())
            setPadding(8, 8, 8, 8)
        }

        // Top bar: Multilingual Quick Board title
        val titleBar = TextView(this).apply {
            text = "⚡ WordBoard Quick Phrases"
            setTextColor(0xFF94A3B8.toInt())
            textSize = 12f
            setPadding(16, 8, 16, 8)
        }
        root.addView(titleBar)

        // Scrollable row of quick phrases
        val scrollView = HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
        }
        snippetsContainer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(8, 8, 8, 16)
        }
        scrollView.addView(snippetsContainer)
        root.addView(scrollView)

        loadSnippetsIntoKeyboard()

        return root
    }

    private fun loadSnippetsIntoKeyboard() {
        serviceScope.launch {
            val db = AppDatabase.getDatabase(this@WordBoardIMEKeyboardService)
            val list = db.snippetDao().getAllSnippetsList()
            withContext(Dispatchers.Main) {
                renderSnippetChips(list)
            }
        }
    }

    private fun renderSnippetChips(snippets: List<SnippetEntity>) {
        val container = snippetsContainer ?: return
        container.removeAllViews()

        for (snippet in snippets) {
            val chip = TextView(this).apply {
                text = "${snippet.shortcut}: ${snippet.label.ifBlank { snippet.expandedText }}"
                setTextColor(0xFFFFFFFF.toInt())
                textSize = 13f
                setBackgroundResource(R.drawable.bg_keyboard_popup_pill)
                setPadding(24, 16, 24, 16)

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 16
                }
                layoutParams = params

                setOnClickListener {
                    currentInputConnection?.commitText(snippet.expandedText, 1)
                }
            }
            container.addView(chip)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
