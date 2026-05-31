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
import com.example.modul5compose.R
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

    fun getAnimeStream(lang: String, query: String = ""): Flow<NetworkResult<List<Anime>>> = flow {
        emit(NetworkResult.Loading)

        if (query.isBlank()) {
            val cached = animeDao.getAllOnce().map { it.toDomain() }
            if (cached.isNotEmpty()) emit(NetworkResult.Success(cached))

            val freshData = fetchRemoteData(lang)
            if (freshData.isNotEmpty()) {
                saveToCache(freshData)
                emit(NetworkResult.Success(freshData))
            } else if (cached.isEmpty()) {
                emit(NetworkResult.Error("Gagal memuat data."))
            }
        } else {
            val searchResults = performSearch(query, lang)
            emit(NetworkResult.Success(searchResults))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun performSearch(query: String, lang: String): List<Anime> = withContext(Dispatchers.IO) {
        val apiLang = if (lang.startsWith("id")) "id-ID" else "en-US"
        runCatching {
            apiService.searchTitle(query = query, language = apiLang)
        }.getOrNull()?.results?.filter { dto ->
            val isAnimation = dto.genreIds.contains(16)
            val isJapanese = dto.originalLanguage == "ja" || dto.originCountry.contains("JP")
            isAnimation && isJapanese
        }?.map { dto ->
            val overview = dto.overview?.takeIf { it.isNotBlank() } ?: "No description available."
            // Jika bahasa Indonesia, gunakan Judul Asli (biasanya Romaji/Jepang), jika Inggris gunakan judul terjemahan
            val displayTitle = if (apiLang == "id-ID") {
                dto.originalTitle ?: dto.originalName ?: dto.title ?: dto.name ?: "Unknown"
            } else {
                dto.title ?: dto.name ?: dto.originalTitle ?: dto.originalName ?: "Unknown"
            }
            
            Anime(
                id = dto.id,
                title = displayTitle,
                year = (dto.releaseDate ?: dto.firstAirDate ?: "").take(4),
                plotId = overview,
                plotEn = overview,
                posterUrl = NetworkModule.posterUrl(dto.posterPath),
                imageRes = R.drawable.img_horimiya,
                url = if (dto.title != null) "https://www.themoviedb.org/movie/${dto.id}" else "https://www.themoviedb.org/tv/${dto.id}"
            )
        } ?: emptyList()
    }

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
                    val displayTitle = if (apiLang == "id-ID") {
                        remote.originalTitle ?: remote.originalName ?: remote.title ?: remote.name ?: base.title
                    } else {
                        remote.title ?: remote.name ?: remote.originalTitle ?: remote.originalName ?: base.title
                    }

                    base.copy(
                        title = displayTitle,
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
