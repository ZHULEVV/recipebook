package com.recipebook.android.presentation.home

import app.cash.turbine.test
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.usecase.GetRecipesUseCase
import com.recipebook.android.domain.usecase.ToggleFavoriteUseCase
import com.recipebook.android.domain.util.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getRecipesUseCase: GetRecipesUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun makeRecipe(id: String, isFavorite: Boolean = false) = Recipe(
        id = id, title = "Рецепт $id", description = "", imageUrl = null,
        cookingTimeMin = 30, baseServings = 2, caloriesPer100g = null,
        proteinPer100g = null, fatPer100g = null, carbsPer100g = null,
        averageRating = null, isFavorite = isFavorite, tags = emptyList(),
        ingredients = emptyList(), steps = emptyList(), authorId = null
    )

    @Test
    fun `recipes loaded successfully on init`() = runTest {
        val recipes = listOf(makeRecipe("1"), makeRecipe("2"))
        every { getRecipesUseCase() } returns flowOf(Resource.Success(recipes))

        val viewModel = HomeViewModel(getRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(recipes, viewModel.uiState.value.recipes)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `error resource sets error state`() = runTest {
        every { getRecipesUseCase() } returns flowOf(Resource.Error("Нет соединения"))

        val viewModel = HomeViewModel(getRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Нет соединения", viewModel.uiState.value.error)
    }

    @Test
    fun `toggleFavorite flips isFavorite optimistically`() = runTest {
        val recipe = makeRecipe("1", isFavorite = false)
        every { getRecipesUseCase() } returns flowOf(Resource.Success(listOf(recipe)))
        coEvery { toggleFavoriteUseCase(any(), any()) } returns Resource.Success(Unit)

        val viewModel = HomeViewModel(getRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.toggleFavorite(recipe)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.recipes.first().isFavorite)
    }
}
