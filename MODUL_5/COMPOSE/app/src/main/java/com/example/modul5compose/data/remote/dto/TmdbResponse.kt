package com.example.modul5compose.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbSearchResponse(
    val results: List<TmdbSearchResultDto> = emptyList()
)

@Serializable
data class TmdbSearchResultDto(
    val id: Int = 0,
    @SerialName("title") val title: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("origin_country") val originCountry: List<String> = emptyList()
)
