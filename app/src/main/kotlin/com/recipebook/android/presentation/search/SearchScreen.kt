package com.recipebook.android.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.recipebook.android.presentation.components.ErrorPlaceholder
import com.recipebook.android.presentation.components.LoadingIndicator
import com.recipebook.android.presentation.components.RecipeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onRecipeClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Поиск") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = viewModel::onQueryChange,
                    placeholder = { Text("Название рецепта...") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = viewModel::search) {
                    Icon(Icons.Outlined.Search, contentDescription = null)
                }
            }
            if (uiState.tags.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.tags, key = { it.id }) { tag ->
                        FilterChip(
                            selected = tag.id in uiState.selectedTagIds,
                            onClick = { viewModel.toggleTag(tag.id) },
                            label = { Text(tag.name) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.error != null -> ErrorPlaceholder(
                    message = uiState.error!!,
                    onRetry = viewModel::search
                )
                !uiState.hasSearched && uiState.history.isNotEmpty() -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("История поиска", style = MaterialTheme.typography.labelMedium)
                        TextButton(onClick = viewModel::clearHistory) { Text("Очистить") }
                    }
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.history, key = { it.id }) { entry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectHistoryQuery(entry.query) }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.History,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = entry.query,
                                    modifier = Modifier.padding(start = 12.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
                uiState.hasSearched -> LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (uiState.results.isEmpty()) {
                        item {
                            Text(
                                text = "Ничего не найдено",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(uiState.results, key = { it.id }) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                isFavorite = recipe.isFavorite,
                                onCardClick = { onRecipeClick(recipe.id) },
                                onFavoriteClick = { viewModel.toggleFavorite(recipe) }
                            )
                        }
                    }
                }
            }
        }
    }
}
