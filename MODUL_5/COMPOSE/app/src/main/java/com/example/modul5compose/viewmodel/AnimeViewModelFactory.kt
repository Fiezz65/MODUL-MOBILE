package com.example.modul5compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AnimeViewModelFactory(private val username: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnimeViewModel(username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}