package com.wordboard.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordboard.app.data.SnippetEntity
import com.wordboard.app.engine.SpellCorrectionEngine
import com.wordboard.app.engine.UniversalExpanderEngine
import com.wordboard.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyboardPopupPreviewScreen(
    snippets: List<SnippetEntity>,
    onBackClick: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var activePopupSnippet by remember { mutableStateOf<SnippetEntity?>(null) }
    var typoCorrection by remember { mutableStateOf<Pair<String, String>?>(null) }
    var messages by remember { mutableStateOf(listOf("Type any shortcut below or test the keyboard popup!")) }

    val spellEngine = remember { SpellCorrectionEngine() }

    // Check on text change for shortcut or typo
    fun checkWord(text: String) {
        val lastWord = text.split(" ", "\n", ",", ".").lastOrNull()?.trim() ?: ""
        if (lastWord.isBlank()) {
            activePopupSnippet = null
            typoCorrection = null
            return
        }

        // 1. Exact shortcut match
        val matched = snippets.firstOrNull { it.shortcut.equals(lastWord, ignoreCase = !it.isCaseSensitive) }
        if (matched != null) {
            activePopupSnippet = matched
            typoCorrection = null
            return
        }

        // 2. Typo match
        val fix = spellEngine.findCorrectionForTypo(lastWord)
        if (fix != null) {
            typoCorrection = Pair(lastWord, fix)
            activePopupSnippet = null
            return
        }

        // 3. Fuzzy shortcut match
        val closest = spellEngine.findClosestShortcut(lastWord, snippets.map { it.shortcut })
        if (closest != null) {
            val fuzzyMatch = snippets.firstOrNull { it.shortcut == closest }
            if (fuzzyMatch != null) {
                typoCorrection = Pair(lastWord, fuzzyMatch.expandedText)
                activePopupSnippet = null
                return
            }
        }

        activePopupSnippet = null
        typoCorrection = null
    }

    fun handleSend() {
        if (inputText.isBlank()) return
        var finalMessage = inputText

        // Smart auto-expand on send
        if (activePopupSnippet != null) {
            val word = activePopupSnippet!!.shortcut
            val replacement = activePopupSnippet!!.expandedText
            val idx = finalMessage.lastIndexOf(word)
            finalMessage = if (idx >= 0) finalMessage.substring(0, idx) + replacement else replacement
        } else if (typoCorrection != null) {
            val word = typoCorrection!!.first
            val replacement = typoCorrection!!.second
            val idx = finalMessage.lastIndexOf(word)
            finalMessage = if (idx >= 0) finalMessage.substring(0, idx) + replacement else replacement
        }

        messages = messages + finalMessage
        inputText = ""
        activePopupSnippet = null
        typoCorrection = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Interactive Keyboard Popup Live Demo", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("See how the popup pill works above your keyboard", fontSize = 11.sp, color = TextMuted)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Simulated Chat Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF0B1120))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    messages.takeLast(4).forEach { msg ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryIndigoDark)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(msg, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Quick Shortcut Chips
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("Tap quick sample to type into keyboard:", color = TextMuted, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("omw", "nmt", "/addr", "teh", "slm").forEach { sample ->
                        SuggestionChip(
                            onClick = {
                                inputText = if (inputText.isEmpty() || inputText.endsWith(" ")) "$inputText$sample" else "$inputText $sample"
                                checkWord(inputText)
                            },
                            label = { Text(sample, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = SurfaceVariantDark),
                            border = SuggestionChipDefaults.suggestionChipBorder(borderColor = BorderStroke, enabled = true)
                        )
                    }
                }
            }

            // Chat Input Field & Send Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                        checkWord(it)
                    },
                    placeholder = { Text("Type here (e.g. omw, nmt, teh)...", color = TextMuted, fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryIndigo,
                        unfocusedBorderColor = BorderStroke,
                        focusedContainerColor = BackgroundDark,
                        unfocusedContainerColor = BackgroundDark
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { handleSend() },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryIndigo)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }

            // THE FLOATING KEYBOARD POP-UP PILL
            AnimatedVisibility(
                visible = activePopupSnippet != null || typoCorrection != null,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(PrimaryIndigo)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (typoCorrection != null) AccentAmber else PrimaryIndigo)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (typoCorrection != null) "Fix Typo" else (activePopupSnippet?.shortcut ?: ""),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = typoCorrection?.second ?: (activePopupSnippet?.expandedText ?: ""),
                                color = TextPrimary,
                                fontSize = 12.sp,
                                maxLines = 1,
                                modifier = Modifier.widthIn(max = 160.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(
                                onClick = {
                                    val replacement = typoCorrection?.second ?: (activePopupSnippet?.expandedText ?: "")
                                    val trigger = typoCorrection?.first ?: (activePopupSnippet?.shortcut ?: "")
                                    val idx = inputText.lastIndexOf(trigger)
                                    inputText = if (idx >= 0) {
                                        inputText.substring(0, idx) + replacement
                                    } else {
                                        replacement
                                    }
                                    activePopupSnippet = null
                                    typoCorrection = null
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Expand", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            IconButton(
                                onClick = {
                                    activePopupSnippet = null
                                    typoCorrection = null
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = TextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // Simulated Mobile Keyboard Layout
            SimulatedKeyboard(
                onKeyPress = { key ->
                    inputText += key
                    checkWord(inputText)
                },
                onBackspace = {
                    if (inputText.isNotEmpty()) {
                        inputText = inputText.dropLast(1)
                        checkWord(inputText)
                    }
                },
                onSpace = {
                    inputText += " "
                    checkWord(inputText)
                }
            )
        }
    }
}
