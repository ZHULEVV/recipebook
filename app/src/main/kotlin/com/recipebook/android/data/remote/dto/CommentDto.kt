package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    val id: String,
    val recipeId: String,
    val userId: String,
    val userName: String,
    val text: String,
    val createdAt: String
)
