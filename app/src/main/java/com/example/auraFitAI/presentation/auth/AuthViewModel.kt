package com.example.auraFitAI.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auraFitAI.domain.repository.AuthRepository
import com.example.auraFitAI.domain.util.NetworkResult
import com.example.auraFitAI.domain.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _loginState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val loginState: StateFlow<UiState<Boolean>> = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val signUpState: StateFlow<UiState<String>> = _signUpState.asStateFlow()

    private val _onboardingState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val onboardingState: StateFlow<UiState<Boolean>> = _onboardingState.asStateFlow()

    fun login(email: String, password: String){
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            when(val result = authRepository.signInWithEmail(email,password)){
                is NetworkResult.Success -> {
                    _loginState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _loginState.value = UiState.Error(result.message ?: "An unhandled login authentication error occurred.")
                }
                is NetworkResult.Loading -> {
                    _loginState.value = UiState.Loading
                }
            }
        }
    }

    fun register(email: String, password: String){
        _signUpState.value = UiState.Loading
        viewModelScope.launch {
            when(val result = authRepository.signUpWithEmail(email,password)){
                is NetworkResult.Success -> {
                    _signUpState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _signUpState.value = UiState.Error(result.message ?: "Registration network operation failed.")
                }
                is NetworkResult.Loading -> {
                    _signUpState.value = UiState.Loading
                }
            }
        }
    }

    fun submitOnboarding(
        uid: String,
        age: Int,
        height: Double,
        weight: Double,
        goal: String
    ) {
        _onboardingState.value = UiState.Loading

        viewModelScope.launch {
            when (val result = authRepository.saveOnboardingData(uid, age, height, weight, goal)) {
                is NetworkResult.Success -> {
                    _onboardingState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _onboardingState.value = UiState.Error(result.message ?: "Failed to synchronize parameters to profile store.")
                }
                is NetworkResult.Loading -> {
                    _onboardingState.value = UiState.Loading
                }
            }
        }
    }
    fun checkUserSession(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}