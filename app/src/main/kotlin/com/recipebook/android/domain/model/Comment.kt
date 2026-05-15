package com.recipebook.android.domain.model

data class Comment(
    val id: String,
    val recipeId: String,
    val userId: String,
    val userName: String,
    val text: String,
    val createdAt: Long
)
