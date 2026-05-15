package com.recipebook.android.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cache")
data class FavoriteCacheEntity(
    @PrimaryKey val recipeId: String
)
