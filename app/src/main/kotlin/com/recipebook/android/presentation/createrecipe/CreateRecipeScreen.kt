package com.recipebook.android.presentation.createrecipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateRecipeScreen(
    onNavigateBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: CreateRecipeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новый рецепт") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            item {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Название рецепта *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = uiState.cookingTimeMin,
                        onValueChange = viewModel::onCookingTimeChange,
                        label = { Text("Время (мин)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = uiState.baseServings,
                        onValueChange = viewModel::onServingsChange,
                        label = { Text("Порций") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
            item {
                DifficultySelector(
                    selected = uiState.difficulty,
                    onSelect = viewModel::onDifficultyChange
                )
            }
            if (uiState.availableTags.isNotEmpty()) {
                item {
                    Text("Теги", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.availableTags.forEach { tag ->
                            FilterChip(
                                selected = tag.id in uiState.selectedTagIds,
                                onClick  = { viewModel.toggleTag(tag.id) },
                                label    = { Text(tag.name) }
                            )
                        }
                    }
                }
            }
            item {
                HorizontalDivider()
                Text(
                    "Ингредиенты",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            itemsIndexed(uiState.ingredientRows) { index, row ->
                IngredientRowItem(
                    row                  = row,
                    availableIngredients = uiState.availableIngredients,
                    onRowChange          = { viewModel.updateIngredientRow(index, it) },
                    onRemove             = { viewModel.removeIngredientRow(index) },
                    canRemove            = uiState.ingredientRows.size > 1
                )
            }
            item {
                TextButton(onClick = viewModel::addIngredientRow) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Добавить ингредиент")
                }
            }
            item {
                HorizontalDivider()
                Text(
                    "Шаги приготовления",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            itemsIndexed(uiState.steps) { index, step ->
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        "${index + 1}.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp, end = 4.dp)
                    )
                    OutlinedTextField(
                        value = step,
                        onValueChange = { viewModel.updateStep(index, it) },
                        label = { Text("Шаг ${index + 1}") },
                        modifier = Modifier.weight(1f),
                        maxLines = 3
                    )
                    if (uiState.steps.size > 1) {
                        IconButton(onClick = { viewModel.removeStep(index) }) {
                            Icon(Icons.Outlined.Delete, contentDescription = null)
                        }
                    }
                }
            }
            item {
                TextButton(onClick = viewModel::addStep) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Добавить шаг")
                }
            }
            if (uiState.error != null) {
                item {
                    Text(
                        uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                Button(
                    onClick = viewModel::save,
                    enabled = !uiState.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isSaving) CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Сохранить рецепт")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DifficultySelector(selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("EASY" to "Лёгкий", "MEDIUM" to "Средний", "HARD" to "Сложный")
    val label = options.firstOrNull { it.first == selected }?.second ?: selected

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Сложность") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (key, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = { onSelect(key); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IngredientRowItem(
    row: IngredientRow,
    availableIngredients: List<com.recipebook.android.domain.model.Ingredient>,
    onRowChange: (IngredientRow) -> Unit,
    onRemove: () -> Unit,
    canRemove: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.weight(2f)
            ) {
                OutlinedTextField(
                    value = row.ingredientName,
                    onValueChange = { query ->
                        onRowChange(row.copy(ingredientName = query, ingredientId = ""))
                        expanded = true
                    },
                    label = { Text("Ингредиент") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    singleLine = true
                )
                val filtered = availableIngredients.filter {
                    it.name.contains(row.ingredientName, ignoreCase = true)
                }.take(8)
                if (filtered.isNotEmpty()) {
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        filtered.forEach { ing ->
                            DropdownMenuItem(
                                text = { Text(ing.name) },
                                onClick = {
                                    onRowChange(row.copy(ingredientId = ing.id, ingredientName = ing.name))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            OutlinedTextField(
                value = row.amount,
                onValueChange = { onRowChange(row.copy(amount = it)) },
                label = { Text("Кол-во") },
                modifier = Modifier.width(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(4.dp))
            OutlinedTextField(
                value = row.unit,
                onValueChange = { onRowChange(row.copy(unit = it)) },
                label = { Text("Ед.") },
                modifier = Modifier.width(64.dp),
                singleLine = true
            )
            if (canRemove) {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Outlined.Delete, contentDescription = null)
                }
            }
        }
    }
}
