package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.ShoppingEntry
import com.recipebook.android.domain.util.Resource

interface ShoppingListRepository {
    suspend fun getShoppingList(from: String, to: String): Resource<List<ShoppingEntry>>
}
