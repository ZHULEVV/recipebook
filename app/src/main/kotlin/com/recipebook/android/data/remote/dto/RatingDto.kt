package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RatingDto(
    val recipeId: String,
    val userId: String,
    val value: Int
)
