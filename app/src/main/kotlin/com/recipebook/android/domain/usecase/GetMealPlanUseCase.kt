package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.MealPlanEntry
import com.recipebook.android.domain.repository.MealPlanRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class GetMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) {
    suspend operator fun invoke(from: String, to: String): Resource<List<MealPlanEntry>> =
        mealPlanRepository.getMealPlan(from, to)
}
