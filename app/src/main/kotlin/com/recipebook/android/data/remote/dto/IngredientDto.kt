package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class IngredientDto(
    val id: String,
    val name: String
)
