package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredientDto(
    val ingredientId: String,
    val name: String,
    val defaultUnit: String,
    val amount: Double,
    val unit: String
)
