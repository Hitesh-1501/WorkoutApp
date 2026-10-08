package com.example.auraFitAI.presentation.home

enum class WorkoutCardType {
    WORKOUT,
    BMI,
    HISTORY,
    SHARE
}

data class HomeWorkoutItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val duration: String,
    val cardType: WorkoutCardType,
    val iconRes: Int,
    val gradientColors: List<Long>
)
