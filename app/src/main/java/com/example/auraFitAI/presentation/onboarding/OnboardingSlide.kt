package com.example.auraFitAI.presentation.onboarding

data class OnboardingSlide(
    val imageRes: Int,
    val title: String,
    val highlightedWord: String,
    val description: String,
    val isFinalSlide: Boolean = false
)