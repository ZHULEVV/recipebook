package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.MealType
import com.recipebook.android.domain.repository.MealPlanRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class AddToMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) {
    suspend operator fun invoke(recipeId: String, date: String, mealType: MealType): Resource<Unit> =
        mealPlanRepository.addToMealPlan(recipeId, date, mealType)
}
