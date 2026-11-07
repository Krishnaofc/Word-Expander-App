package com.wordboard.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordboard.app.data.SnippetEntity
import com.wordboard.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSnippetScreen(
    initialSnippet: SnippetEntity?,
    onSaveSnippet: (SnippetEntity) -> Unit,
    onBackClick: () -> Unit
) {
    var shortcut by remember { mutableStateOf(initialSnippet?.shortcut ?: "") }
    var expandedText by remember { mutableStateOf(initialSnippet?.expandedText ?: "") }
    var label by remember { mutableStateOf(initialSnippet?.label ?: "") }
    var category by remember { mutableStateOf(initialSnippet?.category ?: "General") }
    var languageCode by remember { mutableStateOf(initialSnippet?.languageCode ?: "any") }
    var triggerMode by remember { mutableStateOf(initialSnippet?.triggerMode ?: "POPUP") }
    var isCaseSensitive by remember { mutableStateOf(initialSnippet?.isCaseSensitive ?: false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val quickVariables = listOf("{date}", "{time}", "{clipboard}", "{cursor}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (initialSnippet == null) "New Word Shortcut" else "Edit Shortcut",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (shortcut.trim().isBlank()) {
                                errorMessage = "Shortcut cannot be empty"
                                return@Button
                            }
                            if (expandedText.trim().isBlank()) {
                                errorMessage = "Expanded text cannot be empty"
                                return@Button
                            }
                            val updated = (initialSnippet ?: SnippetEntity(
                                shortcut = shortcut.trim(),
                                expandedText = expandedText.trim()
                            )).copy(
                                shortcut = shortcut.trim(),
                                expandedText = expandedText.trim(),
                                label = label.trim(),
                                category = category.trim().ifBlank { "General" },
                                languageCode = languageCode.trim().ifBlank { "any" },
                                triggerMode = triggerMode,
                                isCaseSensitive = isCaseSensitive
                            )
                            onSaveSnippet(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save")
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color(0xFFEF4444),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Shortcut Trigger Input
            OutlinedTextField(
                value = shortcut,
                onValueChange = {
                    shortcut = it
                    errorMessage = null
                },
                label = { Text("Shortcut Trigger Word") },
                placeholder = { Text("e.g. omw, /addr, nmt, नमस्ते, شكرا") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryIndigo,
                    unfocusedBorderColor = BorderStroke,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                ),
                supportingText = {
                    Text("Can be in any language or script in the world (Devanagari, Arabic, Latin, Kanji, etc.)", color = TextMuted)
                }
            )

            // Label / Title
            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Friendly Name / Title (Optional)") },
                placeholder = { Text("e.g. Office Address, Thank You Note") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryIndigo,
                    unfocusedBorderColor = BorderStroke,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                )
            )

            // Dynamic Variable Chips
            Text("Insert Dynamic Variables:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                quickVariables.forEach { variable ->
                    SuggestionChip(
                        onClick = {
                            expandedText += if (expandedText.endsWith(" ") || expandedText.isEmpty()) variable else " $variable"
                        },
                        label = { Text(variable, fontSize = 11.sp, color = AccentEmerald) },
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = SurfaceDark),
                        border = SuggestionChipDefaults.suggestionChipBorder(borderColor = BorderStroke, enabled = true)
                    )
                }
            }

            // Expanded Text Editor
            OutlinedTextField(
                value = expandedText,
                onValueChange = {
                    expandedText = it
                    errorMessage = null
                },
                label = { Text("Expanded Full Text / Template") },
                placeholder = { Text("Enter the full text, address, or multi-line phrase to insert...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryIndigo,
                    unfocusedBorderColor = BorderStroke,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                )
            )

            // Category & Language
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    placeholder = { Text("General, Work...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryIndigo,
                        unfocusedBorderColor = BorderStroke,
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark
                    )
                )

                OutlinedTextField(
                    value = languageCode,
                    onValueChange = { languageCode = it },
                    label = { Text("Language Code") },
                    placeholder = { Text("en, hi, ar, any") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryIndigo,
                        unfocusedBorderColor = BorderStroke,
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark
                    )
                )
            }

            // Trigger Mode Selection
            Text("Expansion Behavior on Keyboard:", color = TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { triggerMode = "POPUP" }
                    ) {
                        RadioButton(
                            selected = triggerMode == "POPUP",
                            onClick = { triggerMode = "POPUP" },
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryIndigo)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Keyboard Pop-up Pill (Recommended)", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Displays a sleek 1-tap pill above the keyboard when typed", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { triggerMode = "AUTO" }
                    ) {
                        RadioButton(
                            selected = triggerMode == "AUTO",
                            onClick = { triggerMode = "AUTO" },
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryIndigo)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Auto-Expand Immediately", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Expands instantly as soon as space or delimiter is typed", color = TextMuted, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
