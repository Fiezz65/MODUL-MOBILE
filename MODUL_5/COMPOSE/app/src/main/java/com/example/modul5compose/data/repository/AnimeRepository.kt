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
import kotlinx.coroutines.flow.*

class AnimeRepository(
    private val apiService: TmdbApiService,
    private val animeDao: AnimeDao
) {
    private val seedMovies = AnimeData.animeList

    fun getAnimeStream(lang: String, query: String = ""): Flow<NetworkResult<List<Anime>>> = flow {
        if (query.isBlank()) {
            val cached = animeDao.getAllOnce().map { it.toDomain() }
            if (cached.isNotEmpty()) {
                emit(NetworkResult.Success(cached, ""))
            } else {
                emit(NetworkResult.Loading)
            }

            try {
                val freshData = fetchRemoteData(lang)
                saveToCache(freshData)
                emit(NetworkResult.Success(freshData, ""))
            } catch (e: Exception) {
                if (cached.isEmpty()) emit(NetworkResult.Error("Gagal memuat data."))
            }
        } else {
            val results = performSearch(query, lang)
            emit(NetworkResult.Success(results, query))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun performSearch(query: String, lang: String): List<Anime> {
        val apiLang = if (lang.startsWith("id")) "id-ID" else "en-US"
        val response = try { apiService.searchTitle(query, apiLang) } catch (e: Exception) { null }

        val usedIds = mutableSetOf<Int>()
        return response?.results?.filter {
            it.genreIds.contains(16) && (it.originalLanguage == "ja" || it.originCountry.contains("JP"))
        }?.mapNotNull { dto ->
            val seedMatch = seedMovies.find { seed ->
                val dtoTitle = (dto.title ?: dto.name ?: "").lowercase()
                val seedTitle = seed.title.lowercase()
                dtoTitle.contains(seedTitle) || seedTitle.contains(dtoTitle)
            }
            val finalId = seedMatch?.id ?: dto.id
            if (usedIds.contains(finalId)) return@mapNotNull null
            usedIds.add(finalId)

            mapDtoToAnime(dto, seedMatch, apiLang)
        } ?: emptyList()
    }

    private suspend fun fetchRemoteData(lang: String): List<Anime> {
        val apiLang = if (lang.startsWith("id")) "id-ID" else "en-US"
        val currentCache = animeDao.getAllOnce().associateBy { it.id }

        return seedMovies.map { seed ->
            val response = try { apiService.searchTitle(seed.title, apiLang) } catch (e: Exception) { null }
            val remote = response?.results?.pickBestMatch(seed)
            if (remote != null) {
                mapDtoToAnime(remote, seed, apiLang)
            } else {
                currentCache[seed.id]?.toDomain() ?: seed
            }
        }
    }

    private fun mapDtoToAnime(dto: TmdbSearchResultDto, base: Anime?, apiLang: String): Anime {
        val isIndo = apiLang == "id-ID"
        val rawDate = dto.releaseDate ?: dto.firstAirDate ?: ""
        val formattedDate = if (rawDate.contains("-")) {
            val parts = rawDate.split("-")
            if (parts.size == 3) "${parts[2]}-${parts[1]}-${parts[0]}" else rawDate
        } else rawDate

        return Anime(
            id = base?.id ?: dto.id,
            title = if (isIndo) (dto.originalTitle ?: dto.originalName ?: dto.title ?: "Unknown") else (dto.title ?: dto.name ?: dto.originalTitle ?: "Unknown"),
            year = rawDate.take(4),
            releaseDate = if (formattedDate.isNotBlank()) formattedDate else base?.releaseDate ?: "-",
            plotId = if (isIndo) (dto.overview?.takeIf { it.isNotBlank() } ?: base?.plotId ?: "") else (base?.plotId ?: ""),
            plotEn = if (!isIndo) (dto.overview?.takeIf { it.isNotBlank() } ?: base?.plotEn ?: "") else (base?.plotEn ?: ""),
            posterUrl = NetworkModule.posterUrl(dto.posterPath),
            imageRes = base?.imageRes ?: R.drawable.img_horimiya,
            url = if (dto.title != null) "https://www.themoviedb.org/movie/${dto.id}" else "https://www.themoviedb.org/tv/${dto.id}"
        )
    }

    private fun List<TmdbSearchResultDto>.pickBestMatch(seed: Anime): TmdbSearchResultDto? {
        val normalizedSeed = seed.title.lowercase()
        return firstOrNull {
            val resTitle = (it.title ?: it.name ?: "").lowercase()
            val resYear = (it.releaseDate ?: it.firstAirDate ?: "").take(4)
            resYear == seed.year && (resTitle.contains(normalizedSeed) || normalizedSeed.contains(resTitle))
        } ?: firstOrNull { (it.releaseDate ?: it.firstAirDate ?: "").take(4) == seed.year } ?: firstOrNull()
    }

    private suspend fun saveToCache(items: List<Anime>) {
        animeDao.clearAll()
        animeDao.insertAll(items.map { it.toEntity(System.currentTimeMillis()) })
    }
}