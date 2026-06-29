package com.example.auraFitAI.presentation.auth

data class OnboardingSlide(
    val imageRes: Int,
    val title: String,
    val highlightedWord: String,
    val description: String,
    val isFinalSlide: Boolean = false
)