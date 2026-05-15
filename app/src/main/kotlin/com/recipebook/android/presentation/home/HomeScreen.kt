package com.recipebook.android.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.recipebook.android.R
import com.recipebook.android.presentation.components.ErrorPlaceholder
import com.recipebook.android.presentation.components.LoadingIndicator
import com.recipebook.android.presentation.components.RecipeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRecipeClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(innerPadding))
            uiState.error != null -> ErrorPlaceholder(
                message = uiState.error!!,
                onRetry = viewModel::refresh,
                modifier = Modifier.padding(innerPadding)
            )
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(uiState.recipes, key = { it.id }) { recipe ->
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
