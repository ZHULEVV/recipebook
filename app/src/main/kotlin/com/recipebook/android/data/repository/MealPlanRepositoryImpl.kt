package com.recipebook.android.data.repository

import com.recipebook.android.data.local.database.dao.RecipeCacheDao
import com.recipebook.android.data.local.mapper.toDomain
import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.dto.AddToMealPlanRequestDto
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.MealPlanEntry
import com.recipebook.android.domain.model.MealType
import com.recipebook.android.domain.repository.MealPlanRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class MealPlanRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi,
    private val recipeCacheDao: RecipeCacheDao
) : MealPlanRepository {

    override suspend fun getMealPlan(from: String, to: String): Resource<List<MealPlanEntry>> =
        safeApiCall {
            api.getMealPlan(from, to).map { dto ->
                val entry = dto.toDomain()
                if (entry.recipe == null) {
                    val cached = recipeCacheDao.getById(entry.recipeId)?.toDomain()
                    entry.copy(recipe = cached)
                } else entry
            }
        }

    override suspend fun addToMealPlan(
        recipeId: String,
        date: String,
        mealType: MealType
    ): Resource<Unit> = safeApiCall {
        api.addToMealPlan(AddToMealPlanRequestDto(recipeId, date, mealType.name))
    }

    override suspend fun removeFromMealPlan(entryId: String): Resource<Unit> =
        safeApiCall { api.removeFromMealPlan(entryId) }
}
