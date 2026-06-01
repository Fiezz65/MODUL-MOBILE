package com.example.modul5compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modul5compose.data.NetworkResult
import com.example.modul5compose.data.repository.AnimeRepository
import com.example.modul5compose.model.Anime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _state = MutableStateFlow<NetworkResult<List<Anime>>>(NetworkResult.Loading)
    val animeState = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val searchQuery = _query.asStateFlow()

    private var job: Job? = null

    fun loadAnime(lang: String, q: String = _query.value) {
        job?.cancel()
        job = viewModelScope.launch {
            repository.getAnimeStream(lang, q).collect { _state.value = it }
        }
    }

    fun onSearchQueryChanged(newQ: String, lang: String) {
        _query.value = newQ
        job?.cancel()
        if (newQ.isBlank()) {
            loadAnime(lang, "")
        } else {
            job = viewModelScope.launch {
                delay(500)
                loadAnime(lang, newQ)
            }
        }
    }

    fun animeById(id: Int): Anime? {
        val s = _state.value
        return if (s is NetworkResult.Success) s.data.find { it.id == id } else null
    }
}