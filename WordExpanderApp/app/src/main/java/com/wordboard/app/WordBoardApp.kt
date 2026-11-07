package com.wordboard.app

import android.app.Application
import com.wordboard.app.data.AppDatabase

class WordBoardApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize local database on app launch
        AppDatabase.getDatabase(this)
    }
}
