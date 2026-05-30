package com.recipebook.android.presentation.createrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.Ingredient
import com.recipebook.android.domain.model.Tag
import com.recipebook.android.domain.usecase.CreateRecipeUseCase
import com.recipebook.android.domain.usecase.GetIngredientsUseCase
import com.recipebook.android.domain.usecase.GetTagsUseCase
import com.recipebook.android.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class IngredientRow(
    val ingredientId: String = "",
    val ingredientName: String = "",
    val amount: String = "",
    val unit: String = ""
)

data class CreateRecipeUiState(
    val title: String = "",
    val description: String = "",
    val cookingTimeMin: String = "",
    val activeTimeMin: String = "",
    val difficulty: String = "EASY",
    val baseServings: String = "1",
    val selectedTagIds: Set<String> = emptySet(),
    val ingredientRows: List<IngredientRow> = listOf(IngredientRow()),
    val steps: List<String> = listOf(""),
    val availableTags: List<Tag> = emptyList(),
    val availableIngredients: List<Ingredient> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)

@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val createRecipeUseCase: CreateRecipeUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val getIngredientsUseCase: GetIngredientsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateRecipeUiState())
    val uiState: StateFlow<CreateRecipeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val tags = getTagsUseCase()) {
                is Resource.Success -> _uiState.update { it.copy(availableTags = tags.data) }
                else -> {}
            }
            when (val ings = getIngredientsUseCase()) {
                is Resource.Success -> _uiState.update { it.copy(availableIngredients = ings.data) }
                else -> {}
            }
        }
    }

    fun onTitleChange(v: String)       = _uiState.update { it.copy(title = v) }
    fun onDescriptionChange(v: String) = _uiState.update { it.copy(description = v) }
    fun onCookingTimeChange(v: String) = _uiState.update { it.copy(cookingTimeMin = v) }
    fun onActiveTimeChange(v: String)  = _uiState.update { it.copy(activeTimeMin = v) }
    fun onDifficultyChange(v: String)  = _uiState.update { it.copy(difficulty = v) }
    fun onServingsChange(v: String)    = _uiState.update { it.copy(baseServings = v) }

    fun toggleTag(id: String) = _uiState.update { state ->
        val updated = if (id in state.selectedTagIds) state.selectedTagIds - id
        else state.selectedTagIds + id
        state.copy(selectedTagIds = updated)
    }

    fun updateIngredientRow(index: Int, row: IngredientRow) = _uiState.update { state ->
        state.copy(ingredientRows = state.ingredientRows.toMutableList().also { it[index] = row })
    }

    fun addIngredientRow() = _uiState.update { it.copy(ingredientRows = it.ingredientRows + IngredientRow()) }

    fun removeIngredientRow(index: Int) = _uiState.update { state ->
        if (state.ingredientRows.size <= 1) state
        else state.copy(ingredientRows = state.ingredientRows.filterIndexed { i, _ -> i != index })
    }

    fun updateStep(index: Int, text: String) = _uiState.update { state ->
        state.copy(steps = state.steps.toMutableList().also { it[index] = text })
    }

    fun addStep() = _uiState.update { it.copy(steps = it.steps + "") }

    fun removeStep(index: Int) = _uiState.update { state ->
        if (state.steps.size <= 1) state
        else state.copy(steps = state.steps.filterIndexed { i, _ -> i != index })
    }

    fun save() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "Введите название рецепта") }
            return
        }
        val cookingTime = state.cookingTimeMin.toIntOrNull() ?: 0
        val activeTime  = state.activeTimeMin.toIntOrNull()  ?: cookingTime
        val servings    = state.baseServings.toIntOrNull()   ?: 1
        val ingredients = state.ingredientRows.filter { it.ingredientId.isNotBlank() }.map { row ->
            Triple(row.ingredientId, row.amount.toDoubleOrNull() ?: 0.0, row.unit)
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            when (val result = createRecipeUseCase(
                title          = state.title.trim(),
                description    = state.description.trim(),
                cookingTimeMin = cookingTime,
                activeTimeMin  = activeTime,
                difficulty     = state.difficulty,
                baseServings   = servings,
                ingredients    = ingredients,
                tagIds         = state.selectedTagIds.toList(),
                steps          = state.steps.filter { it.isNotBlank() }
            )) {
                is Resource.Success -> _uiState.update { it.copy(isSaving = false, saved = true) }
                is Resource.Error   -> _uiState.update { it.copy(isSaving = false, error = result.message) }
                else                -> _uiState.update { it.copy(isSaving = false) }
            }
        }
    }
}
