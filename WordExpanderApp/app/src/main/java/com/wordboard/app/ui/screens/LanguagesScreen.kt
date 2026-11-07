package com.wordboard.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
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

data class LanguagePack(
    val code: String,
    val flag: String,
    val name: String,
    val nativeName: String,
    val script: String,
    val sampleSnippet: String,
    val phrases: List<SnippetEntity>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagesScreen(
    onInstallPack: (List<SnippetEntity>) -> Unit,
    onBackClick: () -> Unit
) {
    var installedPacks by remember { mutableStateOf(setOf("en", "hi", "ar", "es", "ja")) }

    val languagePacks = listOf(
        LanguagePack(
            code = "hi",
            flag = "🇮🇳",
            name = "Hindi (हिंदी)",
            nativeName = "हिन्दी",
            script = "Devanagari (देवनागरी)",
            sampleSnippet = "nmt -> नमस्ते! आप कैसे हैं?",
            phrases = listOf(
                SnippetEntity(shortcut = "nmt", expandedText = "नमस्ते! आप कैसे हैं?", label = "नमस्ते", category = "Hindi", languageCode = "hi"),
                SnippetEntity(shortcut = "dhny", expandedText = "आपका बहुत-बहुत धन्यवाद!", label = "धन्यवाद", category = "Hindi", languageCode = "hi"),
                SnippetEntity(shortcut = "shubh", expandedText = "हार्दिक बधाई एवं शुभकामनाएं!", label = "शुभकामनाएं", category = "Hindi", languageCode = "hi")
            )
        ),
        LanguagePack(
            code = "en",
            flag = "🇬🇧",
            name = "English",
            nativeName = "English",
            script = "Latin",
            sampleSnippet = "omw -> On my way! See you shortly.",
            phrases = listOf(
                SnippetEntity(shortcut = "omw", expandedText = "On my way! See you shortly.", label = "On my way", category = "General", languageCode = "en"),
                SnippetEntity(shortcut = "ty", expandedText = "Thank you very much!", label = "Thank you", category = "General", languageCode = "en")
            )
        ),
        LanguagePack(
            code = "ar",
            flag = "🇸🇦",
            name = "Arabic (العربية)",
            nativeName = "العربية",
            script = "Arabic (RTL)",
            sampleSnippet = "slm -> السلام عليكم ورحمة الله",
            phrases = listOf(
                SnippetEntity(shortcut = "slm", expandedText = "السلام عليكم ورحمة الله وبركاته", label = "السلام عليكم", category = "Arabic", languageCode = "ar"),
                SnippetEntity(shortcut = "shkr", expandedText = "شكراً جزيلاً لك على دعمك", label = "شكر", category = "Arabic", languageCode = "ar")
            )
        ),
        LanguagePack(
            code = "es",
            flag = "🇪🇸",
            name = "Spanish (Español)",
            nativeName = "Español",
            script = "Latin Extended",
            sampleSnippet = "hla -> ¡Hola! ¿Cómo estás?",
            phrases = listOf(
                SnippetEntity(shortcut = "hla", expandedText = "¡Hola! ¿Cómo estás hoy?", label = "Hola", category = "Spanish", languageCode = "es"),
                SnippetEntity(shortcut = "grc", expandedText = "¡Muchas gracias por su atención!", label = "Gracias", category = "Spanish", languageCode = "es")
            )
        ),
        LanguagePack(
            code = "ja",
            flag = "🇯🇵",
            name = "Japanese (日本語)",
            nativeName = "日本語",
            script = "Kanji / Hiragana / Katakana",
            sampleSnippet = "arig -> 誠にありがとうございます。",
            phrases = listOf(
                SnippetEntity(shortcut = "arig", expandedText = "誠にありがとうございます。", label = "ありがとうございます", category = "Japanese", languageCode = "ja")
            )
        ),
        LanguagePack(
            code = "zh",
            flag = "🇨🇳",
            name = "Chinese (中文)",
            nativeName = "简体中文",
            script = "Hanzi (汉字)",
            sampleSnippet = "xiexie -> 非常感谢您的帮助！",
            phrases = listOf(
                SnippetEntity(shortcut = "xiexie", expandedText = "非常感谢您的帮助！祝一切顺利！", label = "感谢", category = "Chinese", languageCode = "zh")
            )
        ),
        LanguagePack(
            code = "bn",
            flag = "🇧🇩",
            name = "Bengali (বাংলা)",
            nativeName = "বাংলা",
            script = "Bengali (বাংলা লিপি)",
            sampleSnippet = "shubho -> শুভকামনা এবং অভিনন্দন!",
            phrases = listOf(
                SnippetEntity(shortcut = "shubho", expandedText = "শুভকামনা এবং আন্তরিক অভিনন্দন!", label = "শুভকামনা", category = "Bengali", languageCode = "bn"),
                SnippetEntity(shortcut = "dhonnobad", expandedText = "আপনাকে অনেক অনেক ধন্যবাদ!", label = "ধন্যবাদ", category = "Bengali", languageCode = "bn")
            )
        ),
        LanguagePack(
            code = "ru",
            flag = "🇷🇺",
            name = "Russian (Русский)",
            nativeName = "Русский",
            script = "Cyrillic (Кириллица)",
            sampleSnippet = "priv -> Привет! Как твои дела?",
            phrases = listOf(
                SnippetEntity(shortcut = "priv", expandedText = "Привет! Как твои дела? Надеюсь, всё отлично.", label = "Привет", category = "Russian", languageCode = "ru")
            )
        ),
        LanguagePack(
            code = "de",
            flag = "🇩🇪",
            name = "German (Deutsch)",
            nativeName = "Deutsch",
            script = "Latin Extended (Umlaute)",
            sampleSnippet = "vlg -> Viele liebe Grüße!",
            phrases = listOf(
                SnippetEntity(shortcut = "vlg", expandedText = "Viele liebe Grüße und einen schönen Tag noch!", label = "Grüße", category = "German", languageCode = "de")
            )
        ),
        LanguagePack(
            code = "fr",
            flag = "🇫🇷",
            name = "French (Français)",
            nativeName = "Français",
            script = "Latin Extended (Accents)",
            sampleSnippet = "bjr -> Bonjour, j'espère que vous allez bien.",
            phrases = listOf(
                SnippetEntity(shortcut = "bjr", expandedText = "Bonjour, j'espère que vous passez une agréable journée.", label = "Bonjour", category = "French", languageCode = "fr")
            )
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Universal Multi-language Packs", fontWeight = FontWeight.Bold)
                        Text("Supports all world scripts (UTF-8 Unicode)", fontSize = 11.sp, color = AccentEmerald)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("🌍 Global UTF-8 Engine Active", color = AccentEmerald, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "WordBoard recognizes every script on earth: Latin, Devanagari (Hindi/Marathi), Arabic, Chinese, Japanese, Cyrillic, Bengali, Tamil, etc. You can create shortcuts in ANY language!",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            items(languagePacks) { pack ->
                val isInstalled = installedPacks.contains(pack.code)
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(pack.flag, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(pack.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Script: ${pack.script}", color = TextMuted, fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(pack.sampleSnippet, color = PrimaryIndigo, fontSize = 12.sp)
                            }
                        }

                        Button(
                            onClick = {
                                onInstallPack(pack.phrases)
                                installedPacks = installedPacks + pack.code
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isInstalled) SurfaceVariantDark else PrimaryIndigo
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            if (isInstalled) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentEmerald, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ready", fontSize = 11.sp, color = AccentEmerald)
                            } else {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Install", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
