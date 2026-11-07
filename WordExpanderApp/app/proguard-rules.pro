# Proguard rules for WordBoard
-keep class com.wordboard.app.data.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }
-dontwarn androidx.room.**
