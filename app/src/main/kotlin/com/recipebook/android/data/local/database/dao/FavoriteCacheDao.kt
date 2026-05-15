package com.recipebook.android.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.recipebook.android.data.local.database.entity.FavoriteCacheEntity

@Dao
interface FavoriteCacheDao {

    @Query("SELECT recipeId FROM favorite_cache")
    suspend fun getAllFavoriteIds(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cache WHERE recipeId = :recipeId)")
    suspend fun isFavorite(recipeId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteCacheEntity): Long

    @Query("DELETE FROM favorite_cache WHERE recipeId = :recipeId")
    suspend fun delete(recipeId: String): Int

    @Query("DELETE FROM favorite_cache")
    suspend fun clearAll(): Int
}
