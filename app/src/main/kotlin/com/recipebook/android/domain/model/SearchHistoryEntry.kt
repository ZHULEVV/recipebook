package com.recipebook.android.domain.model

data class SearchHistoryEntry(
    val id: Long,
    val query: String,
    val timestamp: Long
)
