package com.recipebook.android.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.model.SearchHistoryEntry
import com.recipebook.android.domain.model.Tag
import com.recipebook.android.domain.usecase.AddSearchHistoryUseCase
import com.recipebook.android.domain.usecase.ClearSearchHistoryUseCase
import com.recipebook.android.domain.usecase.GetSearchHistoryUseCase
import com.recipebook.android.domain.usecase.GetTagsUseCase
import com.recipebook.android.domain.usecase.SearchRecipesUseCase
import com.recipebook.android.domain.usecase.ToggleFavoriteUseCase
import com.recipebook.android.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val tags: List<Tag> = emptyList(),
    val selectedTagIds: Set<String> = emptySet(),
    val results: List<Recipe> = emptyList(),
    val history: List<SearchHistoryEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val addSearchHistoryUseCase: AddSearchHistoryUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var debounceJob: Job? = null

    init {
        loadTags()
        observeHistory()
    }

    private fun loadTags() {
        viewModelScope.launch {
            when (val result = getTagsUseCase()) {
                is Resource.Success -> _uiState.update { it.copy(tags = result.data) }
                else -> {}
            }
        }
    }

    private fun observeHistory() {
        getSearchHistoryUseCase().onEach { list ->
            _uiState.update { it.copy(history = list) }
        }.launchIn(viewModelScope)
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value, error = null) }
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500)
            search()
        }
    }

    fun toggleTag(tagId: String) {
        _uiState.update { state ->
            val updated = if (tagId in state.selectedTagIds)
                state.selectedTagIds - tagId
            else
                state.selectedTagIds + tagId
            state.copy(selectedTagIds = updated)
        }
    }

    fun toggleTagAndSearch(tagId: String) {
        toggleTag(tagId)
        search()
    }

    fun search() {
        val state = _uiState.value
        val query = state.query.trim()
        val selectedTagIds = state.selectedTagIds.toList()
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, hasSearched = true) }
            if (query.isNotBlank()) addSearchHistoryUseCase(query)
            when (val result = searchRecipesUseCase(query, selectedTagIds)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, results = result.data) }
                is Resource.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else                -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun selectHistoryQuery(query: String) {
        _uiState.update { it.copy(query = query) }
        search()
    }

    fun clearHistory() {
        viewModelScope.launch { clearSearchHistoryUseCase() }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    results = state.results.map {
                        if (it.id == recipe.id) it.copy(isFavorite = !it.isFavorite) else it
                    }
                )
            }
            toggleFavoriteUseCase(recipe.id, recipe.isFavorite)
        }
    }
}
