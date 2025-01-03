package com.example.a7minutesworkout

import android.app.Application

class WorkOutApplication : Application() {
    //create the application class and initialize the database
    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}