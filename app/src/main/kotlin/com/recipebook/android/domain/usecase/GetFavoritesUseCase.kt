package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.Recipe
import com.recipebook.android.domain.repository.FavoriteRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<Resource<List<Recipe>>> = favoriteRepository.getFavorites()
}
