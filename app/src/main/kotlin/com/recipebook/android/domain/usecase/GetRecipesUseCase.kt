package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.repository.RecipeRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    operator fun invoke(): Flow<Resource<List<Recipe>>> = recipeRepository.getRecipes()
}
