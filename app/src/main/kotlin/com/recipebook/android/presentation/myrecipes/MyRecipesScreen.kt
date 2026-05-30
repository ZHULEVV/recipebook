package com.recipebook.android.presentation.myrecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.recipebook.android.presentation.components.ErrorPlaceholder
import com.recipebook.android.presentation.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipesScreen(
    onNavigateBack: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onCreateRecipe: () -> Unit,
    viewModel: MyRecipesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои рецепты") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateRecipe) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(innerPadding))
            uiState.error != null -> ErrorPlaceholder(
                message = uiState.error!!,
                onRetry = viewModel::load,
                modifier = Modifier.padding(innerPadding)
            )
            uiState.recipes.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "У вас пока нет рецептов.\nНажмите + чтобы создать первый.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            else -> LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(uiState.recipes, key = { it.id }) { recipe ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onRecipeClick(recipe.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = recipe.title,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { viewModel.delete(recipe.id) }) {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
