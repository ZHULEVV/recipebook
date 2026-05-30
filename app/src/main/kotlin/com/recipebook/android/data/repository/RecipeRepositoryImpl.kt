package com.recipebook.android.data.repository

import com.recipebook.android.data.local.database.dao.FavoriteCacheDao
import com.recipebook.android.data.local.database.dao.RecipeCacheDao
import com.recipebook.android.data.local.mapper.toDomain
import com.recipebook.android.data.local.mapper.toEntity
import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.dto.CreateRecipeRequestDto
import com.recipebook.android.data.remote.dto.RecipeIngredientRequestDto
import com.recipebook.android.data.remote.dto.StepRequestDto
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.repository.RecipeRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi,
    private val recipeCacheDao: RecipeCacheDao,
    private val favoriteCacheDao: FavoriteCacheDao
) : RecipeRepository {

    override fun getRecipes(): Flow<Resource<List<Recipe>>> = flow {
        emit(Resource.Loading)
        try {
            val favoriteIds = favoriteCacheDao.getAllFavoriteIds().toSet()
            val recipes = api.getRecipes().items.map { dto ->
                dto.toDomain().copy(isFavorite = dto.id in favoriteIds)
            }
            val now = System.currentTimeMillis()
            recipeCacheDao.insertAll(recipes.map { it.toEntity(now) })
        } catch (e: Exception) {
            emitAll(
                recipeCacheDao.getAllCached().map { entities ->
                    if (entities.isEmpty()) Resource.Error(e.message ?: "Ошибка загрузки")
                    else Resource.Success(entities.map { it.toDomain() })
                }
            )
            return@flow
        }
        emitAll(
            recipeCacheDao.getAllCached().map { entities ->
                if (entities.isEmpty()) Resource.Empty
                else Resource.Success(entities.map { it.toDomain() })
            }
        )
    }

    override suspend fun getRecipeById(id: String): Resource<Recipe> =
        safeApiCall { api.getRecipeById(id).toDomain() }
            .let { result ->
                if (result is Resource.Success) {
                    recipeCacheDao.insert(result.data.toEntity())
                }
                result
            }.let {
                if (it is Resource.Error) {
                    recipeCacheDao.getById(id)?.toDomain()
                        ?.let { cached -> Resource.Success(cached) } ?: it
                } else it
            }

    override suspend fun searchRecipes(query: String, tagIds: List<String>): Resource<List<Recipe>> =
        if (query.isBlank() && tagIds.isNotEmpty()) {
            safeApiCall { api.getRecipes(tagIds).items.map { it.toDomain() } }
        } else {
            safeApiCall { api.searchRecipes(query, tagIds).items.map { it.toDomain() } }
        }

    override suspend fun refreshRecipes(): Resource<Unit> =
        safeApiCall {
            val recipes = api.getRecipes().items
            val now = System.currentTimeMillis()
            recipeCacheDao.insertAll(recipes.map { it.toDomain().toEntity(now) })
        }

    override suspend fun getMyRecipes(): Resource<List<Recipe>> =
        safeApiCall { api.getMyRecipes().items.map { it.toDomain() } }

    override suspend fun createRecipe(
        title: String,
        description: String,
        cookingTimeMin: Int,
        activeTimeMin: Int,
        difficulty: String,
        baseServings: Int,
        ingredients: List<Triple<String, Double, String>>,
        tagIds: List<String>,
        steps: List<String>
    ): Resource<Recipe> = safeApiCall {
        api.createRecipe(
            CreateRecipeRequestDto(
                title          = title,
                description    = description,
                cookingTimeMin = cookingTimeMin,
                activeTimeMin  = activeTimeMin,
                difficulty     = difficulty,
                baseServings   = baseServings,
                ingredients    = ingredients.map { (id, amount, unit) ->
                    RecipeIngredientRequestDto(id, amount, unit)
                },
                tagIds = tagIds,
                steps  = steps.mapIndexed { i, content -> StepRequestDto(i + 1, content) }
            )
        ).toDomain()
    }

    override suspend fun deleteRecipe(id: String): Resource<Unit> =
        safeApiCall { api.deleteRecipe(id) }
}
