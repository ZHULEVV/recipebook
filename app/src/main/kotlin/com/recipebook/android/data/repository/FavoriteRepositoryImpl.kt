package com.recipebook.android.data.repository

import com.recipebook.android.data.local.database.dao.FavoriteCacheDao
import com.recipebook.android.data.local.database.dao.RecipeCacheDao
import com.recipebook.android.data.local.database.entity.FavoriteCacheEntity
import com.recipebook.android.data.local.mapper.toDomain
import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.repository.FavoriteRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi,
    private val favoriteCacheDao: FavoriteCacheDao,
    private val recipeCacheDao: RecipeCacheDao
) : FavoriteRepository {

    override fun getFavorites(): Flow<Resource<List<Recipe>>> = flow {
        emit(Resource.Loading)
        try {
            val recipes = api.getFavorites().map { it.toDomain().copy(isFavorite = true) }
            val favoriteIds = recipes.map { it.id }
            favoriteCacheDao.clearAll()
            favoriteIds.forEach { id -> favoriteCacheDao.insert(FavoriteCacheEntity(id)) }
            if (recipes.isEmpty()) emit(Resource.Empty)
            else emit(Resource.Success(recipes))
        } catch (e: Exception) {
            emitAll(
                recipeCacheDao.getAllCached().map { entities ->
                    val favoriteIds = favoriteCacheDao.getAllFavoriteIds().toSet()
                    val favorites = entities.filter { it.id in favoriteIds }.map { it.toDomain() }
                    if (favorites.isEmpty()) Resource.Error(e.message ?: "Ошибка загрузки")
                    else Resource.Success(favorites)
                }
            )
        }
    }

    override suspend fun addFavorite(recipeId: String): Resource<Unit> {
        favoriteCacheDao.insert(FavoriteCacheEntity(recipeId))
        recipeCacheDao.updateFavoriteStatus(recipeId, true)
        return safeApiCall { api.addFavorite(recipeId) }
    }

    override suspend fun removeFavorite(recipeId: String): Resource<Unit> {
        favoriteCacheDao.delete(recipeId)
        recipeCacheDao.updateFavoriteStatus(recipeId, false)
        return safeApiCall { api.removeFavorite(recipeId) }
    }

    override suspend fun isFavorite(recipeId: String): Boolean =
        favoriteCacheDao.isFavorite(recipeId)

    override suspend fun syncFavorites(): Resource<Unit> =
        safeApiCall {
            val recipes = api.getFavorites()
            favoriteCacheDao.clearAll()
            recipes.forEach { favoriteCacheDao.insert(FavoriteCacheEntity(it.id)) }
        }
}
