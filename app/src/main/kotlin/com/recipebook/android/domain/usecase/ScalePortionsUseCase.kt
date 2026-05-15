package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.RecipeIngredient
import javax.inject.Inject

class ScalePortionsUseCase @Inject constructor() {
    operator fun invoke(
        ingredients: List<RecipeIngredient>,
        baseServings: Int,
        targetServings: Int
    ): List<RecipeIngredient> {
        if (baseServings == 0) return ingredients
        val factor = targetServings.toDouble() / baseServings
        return ingredients.map { it.copy(amount = it.amount * factor) }
    }
}
