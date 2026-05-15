package com.recipebook.android.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.recipebook.android.data.local.database.dao.FavoriteCacheDao
import com.recipebook.android.data.local.database.dao.RecipeCacheDao
import com.recipebook.android.data.local.database.dao.SearchHistoryDao
import com.recipebook.android.data.local.database.entity.FavoriteCacheEntity
import com.recipebook.android.data.local.database.entity.RecipeCacheEntity
import com.recipebook.android.data.local.database.entity.SearchHistoryEntity

@Database(
    entities = [
        RecipeCacheEntity::class,
        FavoriteCacheEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeCacheDao(): RecipeCacheDao
    abstract fun favoriteCacheDao(): FavoriteCacheDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}
