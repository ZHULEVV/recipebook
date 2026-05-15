package com.recipebook.android.data.repository

import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.Ingredient
import com.recipebook.android.domain.repository.IngredientRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class IngredientRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi
) : IngredientRepository {

    override suspend fun getIngredients(): Resource<List<Ingredient>> =
        safeApiCall { api.getIngredients().map { it.toDomain() } }
}
