package com.recipebook.android.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_cache")
data class RecipeCacheEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val cookingTimeMin: Int,
    val baseServings: Int,
    val caloriesPer100g: Double?,
    val proteinPer100g: Double?,
    val fatPer100g: Double?,
    val carbsPer100g: Double?,
    val averageRating: Double?,
    val isFavorite: Boolean,
    val tagsJson: String,
    val ingredientsJson: String,
    val stepsJson: String,
    val authorId: String?,
    val cachedAt: Long
)
