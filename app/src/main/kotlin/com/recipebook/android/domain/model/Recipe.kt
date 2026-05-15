package com.recipebook.android.domain.model

data class Recipe(
    val id: String,
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
    val tags: List<Tag>,
    val ingredients: List<RecipeIngredient>,
    val steps: List<Step>,
    val authorId: String?
)
