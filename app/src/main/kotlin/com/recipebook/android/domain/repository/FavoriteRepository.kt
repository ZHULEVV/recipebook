package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<Resource<List<Recipe>>>
    suspend fun addFavorite(recipeId: String): Resource<Unit>
    suspend fun removeFavorite(recipeId: String): Resource<Unit>
    suspend fun isFavorite(recipeId: String): Boolean
    suspend fun syncFavorites(): Resource<Unit>
}
