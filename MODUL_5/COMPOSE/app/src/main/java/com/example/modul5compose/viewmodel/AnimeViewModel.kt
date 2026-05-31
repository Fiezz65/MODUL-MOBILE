package com.example.modul5compose.viewmodel

import androidx.lifecycle.ViewModel
import com.example.modul5compose.data.AnimeData
import com.example.modul5compose.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class AnimeViewModel(private val username: String) : ViewModel() {

    private val _animes = MutableStateFlow<List<Anime>>(emptyList())
    val animes: StateFlow<List<Anime>> = _animes.asStateFlow()

    private val _selectedAnime = MutableStateFlow<Anime?>(null)
    val selectedAnime: StateFlow<Anime?> = _selectedAnime.asStateFlow()

    init {
        Timber.d("User: $username - Loading animes")
        loadGames()
    }

    private fun loadGames() {
        val data = AnimeData.animeList
        _animes.value = data
        Timber.d("Anime list loaded: ${data.size} items")
    }

    fun onDetailClicked(anime: Anime) {
        Timber.d("Tombol Detail ditekan untuk: ${anime.title}")

        Timber.d("Navigasi ke halaman Detail dengan data: ID=${anime.id}, Judul=${anime.title}")

        _selectedAnime.value = anime
    }

    fun onExplicitIntentClicked(url: String) {
        Timber.d("Tombol Explicit Intent (Browser) ditekan untuk URL: $url")
    }

    fun clearSelection() {
        _selectedAnime.value = null
    }
}