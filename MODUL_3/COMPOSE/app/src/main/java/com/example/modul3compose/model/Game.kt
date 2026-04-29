package com.example.modul3compose.model

data class Game(
    val id: Int,
    val title: String,
    val year: String,
    val plotId: String,
    val plotEn: String,
    val imageRes: Int,
    val url: String
)