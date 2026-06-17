package com.example.auraFitAI.presentation

import android.app.Application
import com.example.auraFitAI.data.local.HistoryDatabase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltAndroidApp

class WorkOutApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
    val db by lazy {
        HistoryDatabase.Companion.getInstance(this)
    }
}