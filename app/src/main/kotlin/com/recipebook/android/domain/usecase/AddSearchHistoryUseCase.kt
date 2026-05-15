package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class AddSearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(query: String) = searchHistoryRepository.addEntry(query)
}
