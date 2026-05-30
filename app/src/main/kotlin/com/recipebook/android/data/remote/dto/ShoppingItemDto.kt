package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItemDto(
    val ingredientName: String,
    val totalAmount: Double,
    val unit: String
)
