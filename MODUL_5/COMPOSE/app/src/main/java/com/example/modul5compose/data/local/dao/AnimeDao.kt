package com.example.modul5compose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.modul5compose.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime_table ORDER BY id")
    fun observeAll(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime_table ORDER BY id")
    suspend fun getAllOnce(): List<AnimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<AnimeEntity>)

    @Query("DELETE FROM anime_table")
    suspend fun clearAll()
}