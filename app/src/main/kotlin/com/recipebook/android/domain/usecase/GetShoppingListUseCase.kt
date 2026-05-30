package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.ShoppingEntry
import com.recipebook.android.domain.repository.ShoppingListRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class GetShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend operator fun invoke(from: String, to: String): Resource<List<ShoppingEntry>> =
        shoppingListRepository.getShoppingList(from, to)
}
