package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.MealPlanEntry
import com.recipebook.android.domain.model.MealType
import com.recipebook.android.domain.util.Resource

interface MealPlanRepository {
    suspend fun getMealPlan(from: String, to: String): Resource<List<MealPlanEntry>>
    suspend fun addToMealPlan(recipeId: String, date: String, mealType: MealType): Resource<Unit>
    suspend fun removeFromMealPlan(entryId: String): Resource<Unit>
}
