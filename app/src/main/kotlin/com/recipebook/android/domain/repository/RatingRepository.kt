package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.Rating
import com.recipebook.android.domain.util.Resource

interface RatingRepository {
    suspend fun getRating(recipeId: String): Resource<Rating?>
    suspend fun rateRecipe(recipeId: String, value: Int): Resource<Unit>
}
