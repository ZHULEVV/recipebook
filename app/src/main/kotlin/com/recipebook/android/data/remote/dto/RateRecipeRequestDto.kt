package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RateRecipeRequestDto(
    val value: Int
)
