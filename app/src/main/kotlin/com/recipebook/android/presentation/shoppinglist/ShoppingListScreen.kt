package com.recipebook.android.presentation.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.recipebook.android.presentation.components.ErrorPlaceholder
import com.recipebook.android.presentation.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    onNavigateBack: () -> Unit,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hasChecked = uiState.items.any { it.isChecked }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список покупок") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    if (hasChecked) {
                        IconButton(onClick = viewModel::clearChecked) {
                            Icon(Icons.Outlined.DeleteSweep, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(innerPadding))
            uiState.error != null -> ErrorPlaceholder(
                message = uiState.error!!,
                onRetry = viewModel::nextWeek,
                modifier = Modifier.padding(innerPadding)
            )
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    item {
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
                            Text(
                                uiState.weekLabel,
                                style = MaterialTheme.typography.titleSmall
                            )
                            IconButton(onClick = viewModel::nextWeek) {
                                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
                            }
                        }
                    }
                    if (uiState.items.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "На эту неделю список покупок пуст.\nДобавьте рецепты в план питания.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(24.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    } else {
                        itemsIndexed(uiState.items, key = { i, _ -> i }) { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Checkbox(
                                        checked = item.isChecked,
                                        onCheckedChange = { viewModel.toggleItem(index) }
                                    )
                                    Text(
                                        text = item.entry.ingredientName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
                                        color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = "${"%.1f".format(item.entry.totalAmount)} ${item.entry.unit}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
