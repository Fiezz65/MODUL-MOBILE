package com.example.modul3compose.viewmodel

import androidx.lifecycle.ViewModel
import com.example.modul3compose.data.GameData
import com.example.modul3compose.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class GameViewModel(private val username: String) : ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games.asStateFlow()

    private val _selectedGame = MutableStateFlow<Game?>(null)
    val selectedGame: StateFlow<Game?> = _selectedGame.asStateFlow()

    init {
        Timber.d("User: $username - Loading games")
        loadGames()
    }

    private fun loadGames() {
        val data = GameData.gameList
        _games.value = data
        Timber.d("Game list loaded: ${data.size} items")
    }

    fun onDetailClicked(game: Game) {
        Timber.d("Tombol Detail ditekan untuk: ${game.title}")

        Timber.d("Navigasi ke halaman Detail dengan data: ID=${game.id}, Judul=${game.title}")
        
        _selectedGame.value = game
    }

    fun onExplicitIntentClicked(url: String) {
        Timber.d("Tombol Explicit Intent (Browser) ditekan untuk URL: $url")
    }

    fun clearSelection() {
        _selectedGame.value = null
    }
}