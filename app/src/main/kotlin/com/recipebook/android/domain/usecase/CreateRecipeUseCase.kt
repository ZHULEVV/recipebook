package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.repository.RecipeRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class CreateRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        cookingTimeMin: Int,
        activeTimeMin: Int,
        difficulty: String,
        baseServings: Int,
        ingredients: List<Triple<String, Double, String>>,
        tagIds: List<String>,
        steps: List<String>
    ): Resource<Recipe> = recipeRepository.createRecipe(
        title, description, cookingTimeMin, activeTimeMin,
        difficulty, baseServings, ingredients, tagIds, steps
    )
}
