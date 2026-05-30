package com.recipebook.android.data.repository

import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.ShoppingEntry
import com.recipebook.android.domain.repository.ShoppingListRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi
) : ShoppingListRepository {

    override suspend fun getShoppingList(from: String, to: String): Resource<List<ShoppingEntry>> =
        safeApiCall {
            api.getShoppingList(from, to).map { dto ->
                ShoppingEntry(
                    ingredientName = dto.ingredientName,
                    totalAmount    = dto.totalAmount,
                    unit           = dto.unit
                )
            }
        }
}
