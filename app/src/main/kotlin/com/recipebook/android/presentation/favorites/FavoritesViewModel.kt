package com.recipebook.android.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.usecase.GetFavoritesUseCase
import com.recipebook.android.domain.usecase.ToggleFavoriteUseCase
import com.recipebook.android.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        getFavoritesUseCase().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, recipes = resource.data) }
                is Resource.Error   -> _uiState.update { it.copy(isLoading = false, error = resource.message) }
                is Resource.Empty   -> _uiState.update { it.copy(isLoading = false, recipes = emptyList()) }
            }
        }.launchIn(viewModelScope)
    }

    fun removeFromFavorites(recipe: Recipe) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(recipes = state.recipes.filter { it.id != recipe.id })
            }
            toggleFavoriteUseCase(recipe.id, isFavorite = true)
        }
    }
}
