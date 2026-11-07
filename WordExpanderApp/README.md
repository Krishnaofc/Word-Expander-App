# WordBoard: Universal Text Expander & Keyboard Pop-up App

Production-ready Android application built with Kotlin, Jetpack Compose, Material 3, Room SQLite, and Android Accessibility & WindowManager APIs.

Designed for Google Play Store publication with universal multi-language support (all world scripts via UTF-8) and keyboard-level integration.

---

## 📱 Core Features

1. **Universal Multi-Language Support (Every Script on Earth)**
   - Devanagari (Hindi, Marathi, Sanskrit)
   - Arabic & Urdu (Full Right-To-Left RTL)
   - Japanese (Kanji, Hiragana, Katakana)
   - Chinese (Simplified & Traditional Hanzi)
   - Latin Extended (Spanish, French, German, Vietnamese)
   - Cyrillic (Russian, Ukrainian)
   - Bengali, Tamil, Telugu, Thai, Emoji & Symbols

2. **Keyboard Integration & 1-Tap Pop-up Pill**
   - **Accessibility Service (`WordBoardAccessibilityService`)**: Monitors text globally in any app (WhatsApp, Telegram, Gmail, Instagram, Chrome, Notes) without forcing the user to switch keyboards.
   - **Floating Pop-up Overlay (`OverlayPopupManager`)**: Shows a sleek Material 3 pill right above standard keyboards (Gboard, Samsung Keyboard, SwiftKey) when a shortcut or misspelled word is typed.
   - **1-Tap Replacement**: Clicking "Expand" replaces the shortcut with the full expanded template.

3. **Intelligent Spell & Typo Correction**
   - Detects common misspellings (`teh` -> `the`, `recieve` -> `receive`, `nmaste` -> `नमस्ते`).
   - Levenshtein fuzzy distance matching so close typos still trigger the expansion pill.

4. **Dynamic Variables**
   - `{date}`: Inserts formatted date in device locale.
   - `{time}`: Inserts current time.
   - `{clipboard}`: Pastes current clipboard contents safely.
   - `{cursor}`: Directs cursor focus point.

5. **In-App Interactive Simulator**
   - Built-in simulator screen to test typing and watch the floating pop-up pill in action without leaving the app.

---

## 🛠️ Project Structure

```
WordBoardApp/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── res/
│       │   ├── values/ (strings.xml, colors.xml, themes.xml)
│       │   ├── values-hi/ (Hindi localized strings)
│       │   ├── xml/ (accessibility_service_config.xml, method.xml)
│       │   ├── layout/ (view_keyboard_popup_pill.xml)
│       │   └── drawable/ (app icons, pill backgrounds)
│       └── java/com/wordboard/app/
│           ├── WordBoardApp.kt (Application class)
│           ├── data/
│           │   ├── SnippetEntity.kt (Room entity with UTF-8 support)
│           │   ├── SnippetDao.kt
│           │   ├── AppDatabase.kt
│           │   └── PreloadedSnippets.kt (Multilingual default packs)
│           ├── engine/
│           │   ├── UniversalExpanderEngine.kt (Unicode NFKC normalization, delimiters)
│           │   └── SpellCorrectionEngine.kt (Typo & Levenshtein matcher)
│           ├── service/
│           │   ├── WordBoardAccessibilityService.kt (Global text watcher)
│           │   ├── OverlayPopupManager.kt (Floating pill above keyboard)
│           │   ├── FloatingBoardService.kt (Draggable quick-dock bubble)
│           │   └── WordBoardIMEKeyboardService.kt (Direct keyboard option)
│           └── ui/
│               ├── MainActivity.kt
│               ├── theme/ (Color, Theme, Type)
│               └── screens/
│                   ├── HomeScreen.kt (Dashboard, categories, search)
│                   ├── AddEditSnippetScreen.kt (Template editor)
│                   ├── KeyboardPopupPreviewScreen.kt (Interactive simulator)
│                   ├── LanguagesScreen.kt (Multi-language pack manager)
│                   └── PermissionsSetupScreen.kt (Play Store compliant onboarding)
├── demo/
│   └── index.html (Interactive live web demo for immediate browser testing)
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── PLAY_STORE_LISTING.md (Complete ASO metadata & Accessibility declaration)
```

---

## 🚀 How to Run & Build

### 1. Open in Android Studio
1. Open Android Studio (Ladybug / Koala / Hedgehog or newer).
2. Click **File -> Open** and select:
   `C:\Users\gopal\.gemini\antigravity\scratch\WordBoardApp`
3. Wait for Gradle Sync to complete.

### 2. Run on Device / Emulator
1. Connect your Android phone via USB (with USB Debugging enabled) or start an Android Emulator.
2. Click **Run 'app'** (`Shift + F10`).
3. Complete the quick onboarding in the app to enable:
   - **Accessibility Service** (for global shortcut detection)
   - **Display over other apps** (for the keyboard pop-up pill)

### 3. Build Release Bundle for Play Store
In Android Studio:
1. Go to **Build -> Generate Signed Bundle / APK...**
2. Choose **Android App Bundle (.aab)**.
3. Select or create your keystore.
4. Select `release` build variant with Minify & Resource Shrinking enabled.
5. Upload the resulting `.aab` file to **Google Play Console**.

---

## 🌐 Instant Web Demo
Double-click `demo/index.html` or open it in any web browser to test the interactive smartphone simulator, type shortcuts, and see the keyboard pop-up pill in action!
