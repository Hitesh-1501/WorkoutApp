package com.example.auraFitAI.domain.util

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable, val message: String? = exception.localizedMessage) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}