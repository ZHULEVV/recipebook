package com.recipebook.android.presentation.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.RecipeIngredient
import com.recipebook.android.domain.usecase.GetFavoritesUseCase
import com.recipebook.android.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ShoppingItem(
    val ingredient: RecipeIngredient,
    val isChecked: Boolean = false
)

data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    init {
        getFavoritesUseCase().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                is Resource.Success -> {
                    val allIngredients = resource.data
                        .flatMap { it.ingredients }
                        .distinctBy { it.ingredient.id }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            items = allIngredients.map { ri -> ShoppingItem(ri) }
                        )
                    }
                }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = resource.message) }
                is Resource.Empty -> _uiState.update { it.copy(isLoading = false, items = emptyList()) }
            }
        }.launchIn(viewModelScope)
    }

    fun toggleItem(index: Int) {
        _uiState.update { state ->
            state.copy(
                items = state.items.mapIndexed { i, item ->
                    if (i == index) item.copy(isChecked = !item.isChecked) else item
                }
            )
        }
    }

    fun clearChecked() {
        _uiState.update { state ->
            state.copy(items = state.items.filter { !it.isChecked })
        }
    }
}
