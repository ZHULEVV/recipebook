package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddToMealPlanRequestDto(
    val recipeId: String,
    val date: String,
    val mealType: String
)
