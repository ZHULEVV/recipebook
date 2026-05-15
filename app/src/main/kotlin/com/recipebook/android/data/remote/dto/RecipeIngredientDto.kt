package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredientDto(
    val ingredient: IngredientDto,
    val amount: Double,
    val unit: String
)
