package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.Ingredient
import com.recipebook.android.domain.util.Resource

interface IngredientRepository {
    suspend fun getIngredients(): Resource<List<Ingredient>>
}
