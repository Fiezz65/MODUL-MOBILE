package com.example.modul5compose.data.remote.api

import com.example.modul5compose.data.remote.dto.TmdbSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {
    @GET("search/multi")
    suspend fun searchTitle(
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("include_adult") includeAdult: Boolean = false
    ): TmdbSearchResponse
}