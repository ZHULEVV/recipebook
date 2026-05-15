package com.recipebook.android.domain.model

data class RecipeIngredient(
    val ingredient: Ingredient,
    val amount: Double,
    val unit: String
)
