package com.recipebook.android.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.usecase.GetRecipesUseCase
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

data class HomeUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        getRecipesUseCase().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, recipes = resource.data) }
                is Resource.Error   -> _uiState.update { it.copy(isLoading = false, error = resource.message) }
                is Resource.Empty   -> _uiState.update { it.copy(isLoading = false, recipes = emptyList()) }
            }
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    recipes = state.recipes.map {
                        if (it.id == recipe.id) it.copy(isFavorite = !it.isFavorite) else it
                    }
                )
            }
            toggleFavoriteUseCase(recipe.id, recipe.isFavorite)
        }
    }

    fun refresh() {
        loadRecipes()
    }
}
