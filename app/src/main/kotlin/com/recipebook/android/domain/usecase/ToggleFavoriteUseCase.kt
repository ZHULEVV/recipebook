package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.repository.FavoriteRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(recipeId: String, isFavorite: Boolean): Resource<Unit> =
        if (isFavorite) favoriteRepository.removeFavorite(recipeId)
        else favoriteRepository.addFavorite(recipeId)
}
