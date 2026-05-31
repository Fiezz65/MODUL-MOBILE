package com.example.modul5compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modul5compose.data.NetworkResult
import com.example.modul5compose.data.repository.AnimeRepository
import com.example.modul5compose.model.Anime
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimeViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _animeState = MutableStateFlow<NetworkResult<List<Anime>>>(NetworkResult.Loading)
    val animeState: StateFlow<NetworkResult<List<Anime>>> = _animeState.asStateFlow()

    private var loadJob: Job? = null

    init {
    }

    fun loadAnime(lang: String = "en-US") {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getAnimeStream(lang).collect { result ->
                _animeState.value = result
            }
        }
    }

    fun animeById(animeId: Int): Anime? {
        val state = animeState.value
        if (state is NetworkResult.Success) {
            return state.data.firstOrNull { it.id == animeId }
        }
        return null
    }
}