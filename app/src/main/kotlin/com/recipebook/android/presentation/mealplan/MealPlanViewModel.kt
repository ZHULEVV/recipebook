package com.recipebook.android.presentation.mealplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.MealPlanEntry
import com.recipebook.android.domain.model.MealType
import com.recipebook.android.domain.usecase.AddToMealPlanUseCase
import com.recipebook.android.domain.usecase.GetMealPlanUseCase
import com.recipebook.android.domain.usecase.RemoveFromMealPlanUseCase
import com.recipebook.android.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class MealPlanUiState(
    val entries: List<MealPlanEntry> = emptyList(),
    val selectedDate: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val getMealPlanUseCase: GetMealPlanUseCase,
    private val addToMealPlanUseCase: AddToMealPlanUseCase,
    private val removeFromMealPlanUseCase: RemoveFromMealPlanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealPlanUiState())
    val uiState: StateFlow<MealPlanUiState> = _uiState.asStateFlow()

    init {
        getMealPlanUseCase().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, entries = resource.data) }
                is Resource.Error   -> _uiState.update { it.copy(isLoading = false, error = resource.message) }
                is Resource.Empty   -> _uiState.update { it.copy(isLoading = false, entries = emptyList()) }
            }
        }.launchIn(viewModelScope)
    }

    fun selectDate(date: String) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun addEntry(recipeId: String, mealType: MealType) {
        viewModelScope.launch {
            addToMealPlanUseCase(recipeId, _uiState.value.selectedDate, mealType)
        }
    }

    fun removeEntry(entryId: String) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(entries = state.entries.filter { it.id != entryId })
            }
            removeFromMealPlanUseCase(entryId)
        }
    }

    fun entriesForDate(date: String): List<MealPlanEntry> =
        _uiState.value.entries.filter { it.date == date }
}
