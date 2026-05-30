package com.recipebook.android.presentation.mealplan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.recipebook.android.domain.model.MealType
import com.recipebook.android.presentation.components.ErrorPlaceholder
import com.recipebook.android.presentation.components.LoadingIndicator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(
    onRecipeClick: (String) -> Unit,
    onShoppingListClick: () -> Unit,
    viewModel: MealPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weekDates = viewModel.weekDates(uiState.weekOffset)
    val fmt = DateTimeFormatter.ISO_LOCAL_DATE
    val monthLabel = weekDates.first().let {
        "${it.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru")).replaceFirstChar { c -> c.uppercase() }} ${it.year}"
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("План питания") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onShoppingListClick) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
            }
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(innerPadding))
            uiState.error != null -> ErrorPlaceholder(
                message = uiState.error!!,
                onRetry = viewModel::reload,
                modifier = Modifier.padding(innerPadding)
            )
            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = viewModel::prevWeek) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                    Text(monthLabel, style = MaterialTheme.typography.titleSmall)
                    IconButton(onClick = viewModel::nextWeek) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(weekDates) { date ->
                        val dateStr = date.format(fmt)
                        val isSelected = dateStr == uiState.selectedDate
                        val isToday = date == LocalDate.now()
                        val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru"))
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectDate(dateStr) },
                            label = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        dayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isToday) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        date.dayOfMonth.toString(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        )
                    }
                }
                HorizontalDivider()
                val dayEntries = viewModel.entriesForDate(uiState.selectedDate)
                if (dayEntries.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "На этот день нет записей",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        MealType.entries.forEach { mealType ->
                            val group = dayEntries.filter { it.mealType == mealType }
                            if (group.isNotEmpty()) {
                                item {
                                    Text(
                                        mealTypeLabel(mealType),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                                items(group, key = { it.id }) { entry ->
                                    Card(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = entry.recipe?.title ?: entry.recipeId,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.weight(1f)
                                            )
                                            IconButton(onClick = { viewModel.removeEntry(entry.id) }) {
                                                Icon(Icons.Outlined.Delete, contentDescription = null)
                                            }
                                        }
                                    }
                                }
                                item { Spacer(modifier = Modifier.height(4.dp)) }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun mealTypeLabel(mealType: MealType): String = when (mealType) {
    MealType.BREAKFAST -> "Завтрак"
    MealType.LUNCH     -> "Обед"
    MealType.DINNER    -> "Ужин"
    MealType.SNACK     -> "Перекус"
}
