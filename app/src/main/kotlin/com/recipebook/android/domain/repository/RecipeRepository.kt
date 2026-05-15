package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipes(): Flow<Resource<List<Recipe>>>
    suspend fun getRecipeById(id: String): Resource<Recipe>
    suspend fun searchRecipes(query: String, tags: List<String>): Resource<List<Recipe>>
    suspend fun refreshRecipes(): Resource<Unit>
}
