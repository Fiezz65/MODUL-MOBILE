package com.example.modul5compose.data.repository

import com.example.modul5compose.data.AnimeData
import com.example.modul5compose.data.NetworkResult
import com.example.modul5compose.data.local.dao.AnimeDao
import com.example.modul5compose.data.local.entity.toDomain
import com.example.modul5compose.data.local.entity.toEntity
import com.example.modul5compose.data.remote.NetworkModule
import com.example.modul5compose.data.remote.api.TmdbApiService
import com.example.modul5compose.data.remote.dto.TmdbSearchResultDto
import com.example.modul5compose.model.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AnimeRepository(
    private val apiService: TmdbApiService,
    private val animeDao: AnimeDao
) {
    private val seedMovies = AnimeData.animeList

    fun getAnimeStream(lang: String): Flow<NetworkResult<List<Anime>>> = flow {
        emit(NetworkResult.Loading)

        val cached = animeDao.getAllOnce().map { it.toDomain() }
        if (cached.isNotEmpty()) {
            emit(NetworkResult.Success(cached))
        }

        val freshData = fetchRemoteData(lang)
        if (freshData.isNotEmpty()) {
            saveToCache(freshData)
            emit(NetworkResult.Success(freshData))
        } else if (cached.isEmpty()) {
            emit(NetworkResult.Error("Gagal memuat data dari TMDB. Periksa koneksi atau API Key."))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchRemoteData(lang: String): List<Anime> = withContext(Dispatchers.IO) {
        val apiLang = if (lang.startsWith("id")) "id-ID" else "en-US"
        val currentCache = animeDao.getAllOnce().associateBy { it.id }
        
        seedMovies.map { seed ->
            val cached = currentCache[seed.id]?.toDomain()
            runCatching {
                apiService.searchTitle(query = seed.title, language = apiLang)
            }.getOrNull()?.results?.pickBestMatch(seed).let { remote ->
                if (remote == null) {
                    cached ?: seed
                } else {
                    val base = cached ?: seed
                    base.copy(
                        year = (remote.releaseDate ?: remote.firstAirDate)?.take(4) ?: base.year,
                        plotId = if (apiLang.startsWith("id")) {
                            remote.overview?.takeIf { it.isNotBlank() } ?: base.plotId
                        } else base.plotId,
                        plotEn = if (apiLang.startsWith("en")) {
                            remote.overview?.takeIf { it.isNotBlank() } ?: base.plotEn
                        } else base.plotEn,
                        posterUrl = NetworkModule.posterUrl(remote.posterPath) ?: base.posterUrl
                    )
                }
            }
        }
    }

    private fun List<TmdbSearchResultDto>.pickBestMatch(seed: Anime): TmdbSearchResultDto? {
        val normalizedSeedTitle = seed.title.lowercase()
        
        val perfectMatch = firstOrNull { result ->
            val resultYear = (result.releaseDate ?: result.firstAirDate ?: "").take(4)
            val resultTitle = (result.title ?: result.name ?: "").lowercase()
            resultYear == seed.year && (resultTitle.contains(normalizedSeedTitle) || normalizedSeedTitle.contains(resultTitle))
        }
        if (perfectMatch != null) return perfectMatch

        val yearMatch = firstOrNull { result ->
            val resultYear = (result.releaseDate ?: result.firstAirDate ?: "").take(4)
            resultYear == seed.year
        }
        if (yearMatch != null) return yearMatch

        return firstOrNull { result ->
            val resultTitle = (result.title ?: result.name ?: "").lowercase()
            resultTitle.contains(normalizedSeedTitle) || normalizedSeedTitle.contains(resultTitle)
        } ?: firstOrNull()
    }

    private suspend fun saveToCache(items: List<Anime>) {
        animeDao.clearAll()
        animeDao.insertAll(items.map { it.toEntity(System.currentTimeMillis()) })
    }
}