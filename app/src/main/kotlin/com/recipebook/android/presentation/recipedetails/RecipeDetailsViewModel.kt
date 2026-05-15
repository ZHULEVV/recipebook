package com.recipebook.android.presentation.recipedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.model.RecipeIngredient
import com.recipebook.android.domain.usecase.GetRecipeByIdUseCase
import com.recipebook.android.domain.usecase.RateRecipeUseCase
import com.recipebook.android.domain.usecase.ScalePortionsUseCase
import com.recipebook.android.domain.usecase.ToggleFavoriteUseCase
import com.recipebook.android.domain.util.Resource
import com.recipebook.android.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeDetailsUiState(
    val recipe: Recipe? = null,
    val scaledIngredients: List<RecipeIngredient> = emptyList(),
    val targetServings: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userRating: Int = 0
)

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val scalePortionsUseCase: ScalePortionsUseCase,
    private val rateRecipeUseCase: RateRecipeUseCase
) : ViewModel() {

    private val recipeId: String = checkNotNull(savedStateHandle[Screen.RecipeDetails.ARG_RECIPE_ID])

    private val _uiState = MutableStateFlow(RecipeDetailsUiState())
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    init {
        loadRecipe()
    }

    fun loadRecipe() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getRecipeByIdUseCase(recipeId)) {
                is Resource.Success -> {
                    val recipe = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recipe = recipe,
                            targetServings = recipe.baseServings,
                            scaledIngredients = recipe.ingredients
                        )
                    }
                }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun increaseServings() {
        val state = _uiState.value
        val recipe = state.recipe ?: return
        val newServings = state.targetServings + 1
        _uiState.update {
            it.copy(
                targetServings = newServings,
                scaledIngredients = scalePortionsUseCase(recipe.ingredients, recipe.baseServings, newServings)
            )
        }
    }

    fun decreaseServings() {
        val state = _uiState.value
        val recipe = state.recipe ?: return
        if (state.targetServings <= 1) return
        val newServings = state.targetServings - 1
        _uiState.update {
            it.copy(
                targetServings = newServings,
                scaledIngredients = scalePortionsUseCase(recipe.ingredients, recipe.baseServings, newServings)
            )
        }
    }

    fun toggleFavorite() {
        val recipe = _uiState.value.recipe ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(recipe = recipe.copy(isFavorite = !recipe.isFavorite)) }
            toggleFavoriteUseCase(recipe.id, recipe.isFavorite)
        }
    }

    fun rateRecipe(value: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(userRating = value) }
            rateRecipeUseCase(recipeId, value)
        }
    }
}
