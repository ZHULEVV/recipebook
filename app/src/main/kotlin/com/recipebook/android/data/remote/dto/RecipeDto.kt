package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?          = null,
    val cookingTimeMin: Int,
    val baseServings: Int,
    val caloriesPer100g: Double?   = null,
    val proteinPer100g: Double?    = null,
    val fatPer100g: Double?        = null,
    val carbsPer100g: Double?      = null,
    val averageRating: Double?     = null,
    val isFavorite: Boolean        = false,
    val tags: List<TagDto>                  = emptyList(),
    val ingredients: List<RecipeIngredientDto> = emptyList(),
    val steps: List<StepDto>                = emptyList(),
    val authorId: String?          = null
)
