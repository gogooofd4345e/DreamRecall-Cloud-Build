package com.dreamrecall.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDao {

    @Query("SELECT * FROM dreams ORDER BY dateAdded DESC")
    fun getAllDreams(): Flow<List<Dream>>

    // Simple full-text search simulation using LIKE
    @Query("SELECT * FROM dreams WHERE title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY dateAdded DESC")
    fun searchDreams(query: String): Flow<List<Dream>>

    // Get a dream that hasn't been shown recently (to avoid repeating in notification)
    @Query("SELECT * FROM dreams ORDER BY lastShownMillis ASC LIMIT 1")
    suspend fun getLeastRecentlyShownDream(): Dream?

    @Query("SELECT * FROM dreams WHERE id = :id LIMIT 1")
    suspend fun getDreamById(id: Int): Dream?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDream(dream: Dream)

    @Update
    suspend fun updateDream(dream: Dream)

    @Delete
    suspend fun deleteDream(dream: Dream)
}
