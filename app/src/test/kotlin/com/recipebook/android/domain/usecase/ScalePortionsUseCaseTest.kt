package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.Ingredient
import com.recipebook.android.domain.model.RecipeIngredient
import org.junit.Assert.assertEquals
import org.junit.Test

class ScalePortionsUseCaseTest {

    private val useCase = ScalePortionsUseCase()

    private fun ingredient(id: String) = Ingredient(id = id, name = id)

    @Test
    fun `returns original list when baseServings is zero`() {
        val ingredients = listOf(RecipeIngredient(ingredient("flour"), 200.0, "г"))
        val result = useCase(ingredients, baseServings = 0, targetServings = 4)
        assertEquals(ingredients, result)
    }

    @Test
    fun `doubles amounts when target is twice the base`() {
        val ingredients = listOf(
            RecipeIngredient(ingredient("flour"), 100.0, "г"),
            RecipeIngredient(ingredient("sugar"), 50.0, "г")
        )
        val result = useCase(ingredients, baseServings = 2, targetServings = 4)
        assertEquals(200.0, result[0].amount, 0.001)
        assertEquals(100.0, result[1].amount, 0.001)
    }

    @Test
    fun `halves amounts when target is half the base`() {
        val ingredients = listOf(RecipeIngredient(ingredient("milk"), 300.0, "мл"))
        val result = useCase(ingredients, baseServings = 4, targetServings = 2)
        assertEquals(150.0, result[0].amount, 0.001)
    }

    @Test
    fun `returns same amounts when target equals base`() {
        val ingredients = listOf(RecipeIngredient(ingredient("eggs"), 3.0, "шт"))
        val result = useCase(ingredients, baseServings = 3, targetServings = 3)
        assertEquals(3.0, result[0].amount, 0.001)
    }

    @Test
    fun `returns empty list when input is empty`() {
        val result = useCase(emptyList(), baseServings = 2, targetServings = 4)
        assertEquals(emptyList<RecipeIngredient>(), result)
    }
}
