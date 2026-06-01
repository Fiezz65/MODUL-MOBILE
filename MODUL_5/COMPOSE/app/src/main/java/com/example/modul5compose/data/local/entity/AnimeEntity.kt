package com.example.modul5compose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.modul5compose.model.Anime

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: String,
    val releaseDate: String,
    val plotId: String,
    val plotEn: String,
    val imageRes: Int,
    val url: String,
    val posterUrl: String?,
    val updatedAt: Long
)

fun AnimeEntity.toDomain() = Anime(id, title, year, releaseDate, plotId, plotEn, imageRes, url, posterUrl)

fun Anime.toEntity(ts: Long) = AnimeEntity(id, title, year, releaseDate, plotId, plotEn, imageRes, url, posterUrl, ts)