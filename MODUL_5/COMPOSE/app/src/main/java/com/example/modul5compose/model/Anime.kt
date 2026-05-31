package com.example.modul5compose.model

data class Anime(
    val id: Int,
    val title: String,
    val year: String,
    val plotId: String,
    val plotEn: String,
    val imageRes: Int,
    val url: String,
    val posterUrl: String?
)