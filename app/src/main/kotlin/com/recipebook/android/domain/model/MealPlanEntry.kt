package com.recipebook.android.domain.model

data class MealPlanEntry(
    val id: String,
    val recipeId: String,
    val recipe: Recipe?,
    val date: String,
    val mealType: MealType
)
