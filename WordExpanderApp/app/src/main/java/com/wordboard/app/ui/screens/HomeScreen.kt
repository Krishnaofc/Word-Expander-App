package com.wordboard.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.wordboard.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    snippets: List<SnippetEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onAddSnippetClick: () -> Unit,
    onEditSnippetClick: (SnippetEntity) -> Unit,
    onDeleteSnippetClick: (SnippetEntity) -> Unit,
    onOpenSimulatorClick: () -> Unit,
    onOpenLanguagesClick: () -> Unit,
    onOpenPermissionsClick: () -> Unit
) {
    val categories = listOf("All") + snippets.map { it.category }.distinct().filter { it.isNotBlank() }
    val filteredSnippets = snippets.filter { snippet ->
        val matchesCategory = selectedCategory == "All" || snippet.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                snippet.shortcut.contains(searchQuery, ignoreCase = true) ||
                snippet.expandedText.contains(searchQuery, ignoreCase = true) ||
                snippet.label.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryIndigo),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "WordBoard",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Universal Multi-language Expander",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSimulatorClick) {
                        Icon(Icons.Default.Keyboard, contentDescription = "Keyboard Popup Simulator", tint = PrimaryIndigo)
                    }
                    IconButton(onClick = onOpenLanguagesClick) {
                        Icon(Icons.Default.Translate, contentDescription = "Languages", tint = AccentEmerald)
                    }
                    IconButton(onClick = onOpenPermissionsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Setup & Permissions", tint = TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddSnippetClick,
                containerColor = PrimaryIndigo,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Snippet")
            }
        },
        containerColor = BackgroundDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Stats Row Card
            StatsCard(
                totalSnippets = snippets.size,
                totalExpansions = snippets.sumOf { it.usageCount },
                onSimulatorClick = onOpenSimulatorClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search shortcuts in any language (omw, नमस्ते, /addr)...", color = TextMuted, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryIndigo,
                    unfocusedBorderColor = BorderStroke,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelect(category) },
                        label = { Text(category, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryIndigo,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceDark,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) PrimaryIndigo else BorderStroke,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Snippets List
            if (filteredSnippets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No shortcuts found", color = TextSecondary, fontWeight = FontWeight.Medium)
                        Text("Tap + below to create a new universal snippet", color = TextMuted, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredSnippets, key = { it.id }) { snippet ->
                        SnippetItemCard(
                            snippet = snippet,
                            onEditClick = { onEditSnippetClick(snippet) },
                            onDeleteClick = { onDeleteSnippetClick(snippet) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    totalSnippets: Int,
    totalExpansions: Int,
    onSimulatorClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ACTIVE WORD BOARD", color = AccentEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("$totalSnippets Phrases & Shortcuts", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Button(
                    onClick = onSimulatorClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Test Keyboard", fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "Total Expansions", value = "$totalExpansions")
                StatItem(label = "Languages Supported", value = "All (UTF-8)")
                StatItem(label = "Pop-up Overlay", value = "Ready")
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(value, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Text(label, color = TextMuted, fontSize = 10.sp)
    }
}

@Composable
fun SnippetItemCard(
    snippet: SnippetEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Shortcut pill badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PrimaryIndigoDark)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = snippet.shortcut,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (snippet.label.isNotBlank()) {
                        Text(
                            text = snippet.label,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Language tag
                    Text(
                        text = snippet.languageCode.uppercase(),
                        color = AccentEmerald,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceVariantDark)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Mode tag
                    Text(
                        text = if (snippet.triggerMode == "AUTO") "AUTO" else "POPUP",
                        color = AccentAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceVariantDark)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Expanded Preview Text
            Text(
                text = snippet.expandedText,
                color = TextSecondary,
                fontSize = 13.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Used ${snippet.usageCount} times • ${snippet.category}",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Row {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
