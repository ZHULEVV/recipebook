package com.recipebook.android.data.repository

import com.recipebook.android.data.local.database.dao.SearchHistoryDao
import com.recipebook.android.data.local.database.entity.SearchHistoryEntity
import com.recipebook.android.data.local.mapper.toDomain
import com.recipebook.android.domain.model.SearchHistoryEntry
import com.recipebook.android.domain.repository.SearchHistoryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {

    override fun getHistory(): Flow<List<SearchHistoryEntry>> =
        searchHistoryDao.getHistory().map { list -> list.map { it.toDomain() } }

    override suspend fun addEntry(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return
        if (searchHistoryDao.getCount() >= 10) {
            searchHistoryDao.deleteOldest()
        }
        searchHistoryDao.insertEntry(
            SearchHistoryEntity(query = trimmed, timestamp = System.currentTimeMillis())
        )
    }

    override suspend fun clearHistory() {
        searchHistoryDao.clearAll()
    }
}
