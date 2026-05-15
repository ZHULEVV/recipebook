package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.repository.RatingRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class RateRecipeUseCase @Inject constructor(
    private val ratingRepository: RatingRepository
) {
    suspend operator fun invoke(recipeId: String, value: Int): Resource<Unit> =
        ratingRepository.rateRecipe(recipeId, value)
}
