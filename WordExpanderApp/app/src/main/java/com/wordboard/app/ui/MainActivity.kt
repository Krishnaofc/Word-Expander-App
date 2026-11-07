package com.wordboard.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.wordboard.app.data.AppDatabase
import com.wordboard.app.data.SnippetEntity
import com.wordboard.app.ui.screens.*
import com.wordboard.app.ui.theme.WordBoardTheme
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    data class AddEdit(val snippet: SnippetEntity? = null) : Screen()
    object Simulator : Screen()
    object Languages : Screen()
    object Permissions : Screen()
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val snippetDao = database.snippetDao()

        setContent {
            WordBoardTheme {
                val coroutineScope = rememberCoroutineScope()
                val snippets by snippetDao.getAllSnippets().collectAsState(initial = emptyList())

                var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
                var searchQuery by remember { mutableStateOf("") }
                var selectedCategory by remember { mutableStateOf("All") }

                when (val screen = currentScreen) {
                    is Screen.Home -> {
                        HomeScreen(
                            snippets = snippets,
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it },
                            selectedCategory = selectedCategory,
                            onCategorySelect = { selectedCategory = it },
                            onAddSnippetClick = { currentScreen = Screen.AddEdit(null) },
                            onEditSnippetClick = { snippet -> currentScreen = Screen.AddEdit(snippet) },
                            onDeleteSnippetClick = { snippet ->
                                coroutineScope.launch { snippetDao.delete(snippet) }
                            },
                            onOpenSimulatorClick = { currentScreen = Screen.Simulator },
                            onOpenLanguagesClick = { currentScreen = Screen.Languages },
                            onOpenPermissionsClick = { currentScreen = Screen.Permissions }
                        )
                    }

                    is Screen.AddEdit -> {
                        AddEditSnippetScreen(
                            initialSnippet = screen.snippet,
                            onSaveSnippet = { snippet ->
                                coroutineScope.launch {
                                    snippetDao.insert(snippet)
                                    currentScreen = Screen.Home
                                }
                            },
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }

                    is Screen.Simulator -> {
                        KeyboardPopupPreviewScreen(
                            snippets = snippets,
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }

                    is Screen.Languages -> {
                        LanguagesScreen(
                            onInstallPack = { packPhrases ->
                                coroutineScope.launch {
                                    snippetDao.insertAll(packPhrases)
                                }
                            },
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }

                    is Screen.Permissions -> {
                        PermissionsSetupScreen(
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                }
            }
        }
    }
}
