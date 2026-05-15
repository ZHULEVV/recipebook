package com.recipebook.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddCommentRequestDto(
    val text: String
)
