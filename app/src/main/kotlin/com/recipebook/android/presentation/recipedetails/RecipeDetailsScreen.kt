package com.recipebook.android.presentation.recipedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.recipebook.android.R
import com.recipebook.android.presentation.components.ErrorPlaceholder
import com.recipebook.android.presentation.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    onNavigateBack: () -> Unit,
    onCommentsClick: (String) -> Unit,
    viewModel: RecipeDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.recipe?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::toggleFavorite) {
                        Icon(
                            if (uiState.recipe?.isFavorite == true) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = if (uiState.recipe?.isFavorite == true) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(innerPadding))
            uiState.error != null -> ErrorPlaceholder(
                message = uiState.error!!,
                onRetry = viewModel::loadRecipe,
                modifier = Modifier.padding(innerPadding)
            )
            uiState.recipe != null -> {
                val recipe = uiState.recipe!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        AsyncImage(
                            model = recipe.imageUrl,
                            contentDescription = recipe.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        )
                    }
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(recipe.title, style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                stringResource(R.string.label_min, recipe.cookingTimeMin),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (recipe.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(recipe.description, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    if (recipe.tags.isNotEmpty()) {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(recipe.tags, key = { it.id }) { tag ->
                                    AssistChip(onClick = {}, label = { Text(tag.name) })
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    item {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Порции:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = viewModel::decreaseServings) {
                                Icon(Icons.Outlined.Remove, contentDescription = null)
                            }
                            Text(
                                "${uiState.targetServings}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.width(32.dp)
                            )
                            IconButton(onClick = viewModel::increaseServings) {
                                Icon(Icons.Outlined.Add, contentDescription = null)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    if (uiState.scaledIngredients.isNotEmpty()) {
                        item {
                            Text(
                                "Ингредиенты",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(uiState.scaledIngredients) { ri ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(ri.ingredient.name, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "${"%.1f".format(ri.amount)} ${ri.unit}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.height(12.dp)) }
                    }
                    if (recipe.steps.isNotEmpty()) {
                        item {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Приготовление",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        itemsIndexed(recipe.steps) { index, step ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    "${index + 1}.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.width(28.dp)
                                )
                                Text(step.content, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        item { Spacer(modifier = Modifier.height(12.dp)) }
                    }
                    item {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Оценить рецепт",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            (1..5).forEach { star ->
                                IconButton(
                                    onClick = { viewModel.rateRecipe(star) },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        if (star <= uiState.userRating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.OutlinedButton(
                            onClick = { onCommentsClick(recipe.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text("Комментарии")
                        }
                    }
                }
            }
        }
    }
}
