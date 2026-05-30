package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipeRequestDto(
    val title: String,
    val description: String,
    val cookingTimeMin: Int,
    val activeTimeMin: Int,
    val difficulty: String,
    val baseServings: Int,
    val imageUrl: String? = null,
    val ingredients: List<RecipeIngredientRequestDto>,
    val tagIds: List<String>,
    val steps: List<StepRequestDto>
)

@Serializable
data class RecipeIngredientRequestDto(
    val ingredientId: String,
    val amount: Double,
    val unit: String
)

@Serializable
data class StepRequestDto(
    val stepNumber: Int,
    val content: String
)
