package com.example.auraFitAI.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auraFitAI.data.local.HistoryDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class UserProfileUiState(
    val email: String = "",
    val age: Int = 0,
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val gender: String = "",
    val activityLevel: String = "",
    val fitnessGoal: String = "",
    val totalWorkouts: Int = 0,
    val streakDays: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val historyDao: HistoryDao,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
        observeWorkoutHistory()
    }

    private fun loadUserProfile() {
        val currentUser = firebaseAuth.currentUser
        val email = currentUser?.email ?: ""
        val uid = currentUser?.uid

        _uiState.value = _uiState.value.copy(email = email)

        if (uid != null) {
            viewModelScope.launch {
                try {
                    val doc = firestore.collection("users").document(uid).get().await()
                    if (doc.exists()) {
                        val age = doc.getLong("age")?.toInt() ?: 0
                        val height = doc.getDouble("height") ?: 0.0
                        val weight = doc.getDouble("weight") ?: 0.0
                        val gender = doc.getString("gender") ?: "Not Specified"
                        val activityLevel = doc.getString("activityLevel") ?: "Moderate"
                        val fitnessGoal = doc.getString("fitnessGoal") ?: "General Fitness"

                        _uiState.value = _uiState.value.copy(
                            age = age,
                            height = height,
                            weight = weight,
                            gender = gender,
                            activityLevel = activityLevel,
                            fitnessGoal = fitnessGoal
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun observeWorkoutHistory() {
        viewModelScope.launch {
            historyDao.fetchAllDates().collect { historyList ->
                val total = historyList.size
                val streak = if (total > 0) total else 0
                _uiState.value = _uiState.value.copy(
                    totalWorkouts = total,
                    streakDays = streak,
                    isLoading = false
                )
            }
        }
    }
}
