package com.example.modul5compose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.modul5compose.model.Anime

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: String,
    val plotId: String,
    val plotEn: String,
    val imageRes: Int,
    val url: String,
    val posterUrl: String?,
    val updatedAt: Long
)

fun AnimeEntity.toDomain(): Anime {
    return Anime(
        id = id,
        title = title,
        year = year,
        plotId = plotId,
        plotEn = plotEn,
        imageRes = imageRes,
        url = url,
        posterUrl = posterUrl
    )
}

fun Anime.toEntity(updatedAt: Long): AnimeEntity {
    return AnimeEntity(
        id = id,
        title = title,
        year = year,
        plotId = plotId,
        plotEn = plotEn,
        imageRes = imageRes,
        url = url,
        posterUrl = posterUrl,
        updatedAt = updatedAt
    )
}