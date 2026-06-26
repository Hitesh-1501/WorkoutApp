package com.example.auraFitAI.data.repository

import com.example.auraFitAI.domain.AuthRepository
import com.example.auraFitAI.domain.util.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepository {

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): NetworkResult<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email,password).await()
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Error(e,e.localizedMessage?: "Authentication failed. Please verify credentials.")
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): NetworkResult<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            val uid = result.user?.uid ?: throw  Exception("User identification token generation failed.")
            NetworkResult.Success(uid)
        } catch (e: Exception) {
            NetworkResult.Error(e,e.localizedMessage?: "Registration failed. Account might already exist.")
        }
    }

    override suspend fun saveOnboardingData(
        uid: String,
        age: Int,
        height: Double,
        weight: Double,
        fitnessGoal: String
    ): NetworkResult<Boolean> {
        return try {
            val userMetrics = hashMapOf(
                "uid" to uid,
                "age" to age,
                "height" to height,
                "weight" to weight,
                "fitnessGoal" to fitnessGoal,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("users").document(uid).set(userMetrics).await()
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Error(e, e.localizedMessage ?: "Failed to sync profile metrics to cloud storage.")
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun logoutUser() {
       firebaseAuth.signOut()
    }
}