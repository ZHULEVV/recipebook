package com.recipebook.android.presentation.myrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.usecase.DeleteMyRecipeUseCase
import com.recipebook.android.domain.usecase.GetMyRecipesUseCase
import com.recipebook.android.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyRecipesUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val getMyRecipesUseCase: GetMyRecipesUseCase,
    private val deleteMyRecipeUseCase: DeleteMyRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyRecipesUiState())
    val uiState: StateFlow<MyRecipesUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getMyRecipesUseCase()) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, recipes = result.data) }
                is Resource.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else                -> _uiState.update { it.copy(isLoading = false, recipes = emptyList()) }
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(recipes = state.recipes.filter { it.id != id }) }
            deleteMyRecipeUseCase(id)
        }
    }
}
