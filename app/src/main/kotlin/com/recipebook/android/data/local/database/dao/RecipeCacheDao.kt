package com.recipebook.android.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.recipebook.android.data.local.database.entity.RecipeCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeCacheDao {

    @Query("SELECT * FROM recipe_cache ORDER BY cachedAt DESC")
    fun getAllCached(): Flow<List<RecipeCacheEntity>>

    @Query("SELECT * FROM recipe_cache WHERE id = :id")
    suspend fun getById(id: String): RecipeCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeCacheEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeCacheEntity): Long

    @Query("UPDATE recipe_cache SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean): Int

    @Query("DELETE FROM recipe_cache WHERE cachedAt < :expiryTime")
    suspend fun deleteExpired(expiryTime: Long): Int

    @Query("DELETE FROM recipe_cache")
    suspend fun clearAll(): Int
}
