package com.example.modul5compose.data.remote

import com.example.modul5compose.BuildConfig
import com.example.modul5compose.data.remote.api.TmdbApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val authInterceptor = Interceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_READ_TOKEN}")
            .addHeader("accept", "application/json")
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiService: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }

    fun provideApiService(): TmdbApiService = apiService

    fun posterUrl(path: String?): String? {
        if (path.isNullOrBlank()) return null
        return "$IMAGE_BASE_URL$path"
    }
}