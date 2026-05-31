package com.example.modul5compose.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.modul5compose.data.local.database.AnimeDatabase
import com.example.modul5compose.data.remote.NetworkModule
import com.example.modul5compose.data.repository.AnimeRepository

class AnimeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeViewModel::class.java)) {
            val database = AnimeDatabase.getInstance(context)
            val repository = AnimeRepository(
                apiService = NetworkModule.provideApiService(),
                animeDao = database.animeDao()
            )
            @Suppress("UNCHECKED_CAST")
            return AnimeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}