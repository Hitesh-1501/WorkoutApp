package com.example.a7minutesworkout.data.local

import android.app.Application

class WorkOutApplication : Application() {
    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}