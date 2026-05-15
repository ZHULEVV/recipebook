package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.repository.MealPlanRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class RemoveFromMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) {
    suspend operator fun invoke(entryId: String): Resource<Unit> =
        mealPlanRepository.removeFromMealPlan(entryId)
}
