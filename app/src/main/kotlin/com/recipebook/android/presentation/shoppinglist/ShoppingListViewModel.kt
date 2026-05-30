package com.recipebook.android.presentation.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipebook.android.domain.model.ShoppingEntry
import com.recipebook.android.domain.usecase.GetShoppingListUseCase
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

data class ShoppingItem(
    val entry: ShoppingEntry,
    val isChecked: Boolean = false
)

data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val weekLabel: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val getShoppingListUseCase: GetShoppingListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    private var weekOffset = 0

    init {
        load()
    }

    private fun weekRange(offset: Int): Pair<String, String> {
        val fmt = DateTimeFormatter.ISO_LOCAL_DATE
        val base = LocalDate.now().plusWeeks(offset.toLong())
        val monday = base.minusDays((base.dayOfWeek.value - 1).toLong())
        return monday.format(fmt) to monday.plusDays(6).format(fmt)
    }

    private fun load() {
        val (from, to) = weekRange(weekOffset)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, weekLabel = "$from — $to") }
            when (val result = getShoppingListUseCase(from, to)) {
                is Resource.Success -> _uiState.update {
                    it.copy(isLoading = false, items = result.data.map { e -> ShoppingItem(e) })
                }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> _uiState.update { it.copy(isLoading = false, items = emptyList()) }
            }
        }
    }

    fun nextWeek() { weekOffset++; load() }
    fun prevWeek() { weekOffset--; load() }

    fun toggleItem(index: Int) {
        _uiState.update { state ->
            state.copy(items = state.items.mapIndexed { i, item ->
                if (i == index) item.copy(isChecked = !item.isChecked) else item
            })
        }
    }

    fun clearChecked() {
        _uiState.update { state -> state.copy(items = state.items.filter { !it.isChecked }) }
    }
}
