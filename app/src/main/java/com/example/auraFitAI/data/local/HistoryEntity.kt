package com.example.auraFitAI.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("history-table")
data class HistoryEntity(
    @PrimaryKey
    val date : String
)
