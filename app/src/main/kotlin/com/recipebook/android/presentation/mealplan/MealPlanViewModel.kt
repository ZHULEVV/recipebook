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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class MealPlanUiState(
    val entries: List<MealPlanEntry> = emptyList(),
    val selectedDate: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
    val weekOffset: Int = 0,
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
        loadWeek()
    }

    private fun weekRange(offset: Int): Pair<String, String> {
        val fmt = DateTimeFormatter.ISO_LOCAL_DATE
        val base = LocalDate.now().plusWeeks(offset.toLong())
        val monday = base.minusDays((base.dayOfWeek.value - 1).toLong())
        return monday.format(fmt) to monday.plusDays(6).format(fmt)
    }

    fun weekDates(offset: Int): List<LocalDate> {
        val (fromStr, _) = weekRange(offset)
        val monday = LocalDate.parse(fromStr)
        return (0..6).map { monday.plusDays(it.toLong()) }
    }

    private fun loadWeek() {
        val (from, to) = weekRange(_uiState.value.weekOffset)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getMealPlanUseCase(from, to)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, entries = result.data) }
                is Resource.Error   -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else                -> _uiState.update { it.copy(isLoading = false, entries = emptyList()) }
            }
        }
    }

    fun reload() = loadWeek()

    fun nextWeek() {
        _uiState.update { it.copy(weekOffset = it.weekOffset + 1) }
        loadWeek()
    }

    fun prevWeek() {
        _uiState.update { it.copy(weekOffset = it.weekOffset - 1) }
        loadWeek()
    }

    fun selectDate(date: String) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun addEntry(recipeId: String, mealType: MealType) {
        viewModelScope.launch {
            addToMealPlanUseCase(recipeId, _uiState.value.selectedDate, mealType)
            loadWeek()
        }
    }

    fun removeEntry(entryId: String) {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(entries = state.entries.filter { it.id != entryId }) }
            removeFromMealPlanUseCase(entryId)
        }
    }

    fun entriesForDate(date: String): List<MealPlanEntry> =
        _uiState.value.entries.filter { it.date == date }
}
