package com.example.modul5compose.data

sealed class NetworkResult<out T> {
    object Loading : NetworkResult<Nothing>()
    data class Success<out T>(val data: T, val query: String = "") : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}