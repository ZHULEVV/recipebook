package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MealPlanEntryDto(
    val id: String,
    val recipeId: String,
    val recipe: RecipeDto? = null,
    val date: String,
    val mealType: String
)
