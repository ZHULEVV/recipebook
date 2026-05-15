package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class ClearSearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke() = searchHistoryRepository.clearHistory()
}
