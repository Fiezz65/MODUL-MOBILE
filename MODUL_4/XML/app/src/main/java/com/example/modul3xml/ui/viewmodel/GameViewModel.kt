package com.example.modul3xml.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modul3xml.data.Game
import com.example.modul3xml.data.GameData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class GameViewModel(private val category: String) : ViewModel() {

    private val _gameList = MutableStateFlow<List<Game>>(emptyList())
    val gameList: StateFlow<List<Game>> = _gameList.asStateFlow()

    private val _navigateToDetail = MutableStateFlow<Game?>(null)
    val navigateToDetail: StateFlow<Game?> = _navigateToDetail.asStateFlow()

    init {
        loadGames()
    }

    private fun loadGames() {
        viewModelScope.launch {
            val data = GameData.gameList
            _gameList.value = data
            Timber.d("Data game berhasil dimuat ke ViewModel: ${data.size} item")
        }
    }

    fun onDetailClicked(game: Game) {
        Timber.d("Tombol Detail ditekan untuk game: ${game.title}")
        _navigateToDetail.value = game
    }

    fun onExplicitIntentClicked(game: Game) {
        Timber.d("Tombol Explicit Intent ditekan untuk game: ${game.title}")
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
    
    fun getCategory(): String = category
}