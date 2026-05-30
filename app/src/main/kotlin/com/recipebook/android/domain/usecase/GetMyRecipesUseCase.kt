package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.repository.RecipeRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class GetMyRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(): Resource<List<Recipe>> = recipeRepository.getMyRecipes()
}
