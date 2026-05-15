package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StepDto(
    val stepNumber: Int,
    val content: String,
    val imageUrl: String? = null
)
