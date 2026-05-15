package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.SearchHistoryEntry
import com.recipebook.android.domain.repository.SearchHistoryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    operator fun invoke(): Flow<List<SearchHistoryEntry>> = searchHistoryRepository.getHistory()
}
