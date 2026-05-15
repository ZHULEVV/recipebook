package com.recipebook.android.data.repository

import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.dto.AddToMealPlanRequestDto
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.MealPlanEntry
import com.recipebook.android.domain.model.MealType
import com.recipebook.android.domain.repository.MealPlanRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MealPlanRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi
) : MealPlanRepository {

    override fun getMealPlan(): Flow<Resource<List<MealPlanEntry>>> = flow {
        emit(Resource.Loading)
        try {
            val entries = api.getMealPlan().map { it.toDomain() }
            if (entries.isEmpty()) emit(Resource.Empty)
            else emit(Resource.Success(entries))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Ошибка загрузки"))
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

    override suspend fun getMealPlanByDate(date: String): Resource<List<MealPlanEntry>> =
        safeApiCall { api.getMealPlanByDate(date).map { it.toDomain() } }
}
