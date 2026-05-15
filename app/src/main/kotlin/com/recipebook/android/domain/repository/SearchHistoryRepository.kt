package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.SearchHistoryEntry
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getHistory(): Flow<List<SearchHistoryEntry>>
    suspend fun addEntry(query: String)
    suspend fun clearHistory()
}
