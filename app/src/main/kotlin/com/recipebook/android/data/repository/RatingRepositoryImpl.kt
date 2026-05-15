package com.recipebook.android.data.repository

import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.dto.RateRecipeRequestDto
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.Rating
import com.recipebook.android.domain.repository.RatingRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi
) : RatingRepository {

    override suspend fun getRating(recipeId: String): Resource<Rating?> =
        safeApiCall { api.getRating(recipeId)?.toDomain() }

    override suspend fun rateRecipe(recipeId: String, value: Int): Resource<Unit> =
        safeApiCall { api.rateRecipe(recipeId, RateRecipeRequestDto(value)) }
}
