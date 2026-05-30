package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.Ingredient
import com.recipebook.android.domain.repository.IngredientRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class GetIngredientsUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository
) {
    suspend operator fun invoke(): Resource<List<Ingredient>> =
        ingredientRepository.getIngredients()
}
