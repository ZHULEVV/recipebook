package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.MealPlanEntry
import com.recipebook.android.domain.repository.MealPlanRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) {
    operator fun invoke(): Flow<Resource<List<MealPlanEntry>>> = mealPlanRepository.getMealPlan()
}
