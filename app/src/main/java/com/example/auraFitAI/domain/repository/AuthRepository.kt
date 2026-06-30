package com.example.auraFitAI.domain.repository

import com.example.auraFitAI.domain.util.NetworkResult

interface AuthRepository {
    suspend fun signInWithEmail(
        email: String,
        password: String
    ): NetworkResult<Boolean>

    suspend fun signUpWithEmail(
        email: String,
        password: String
    ): NetworkResult<String>


    suspend fun saveOnboardingData(
        uid: String,
        age: Int,
        height: Double,
        weight: Double,
        fitnessGoal: String
    ): NetworkResult<Boolean>

    fun isUserLoggedIn(): Boolean

    fun logoutUser()
}