package com.recipebook.android.domain.model

data class Rating(
    val recipeId: String,
    val userId: String,
    val value: Int
)
