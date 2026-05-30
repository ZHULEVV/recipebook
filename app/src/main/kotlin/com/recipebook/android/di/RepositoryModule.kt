package com.recipebook.android.di

import com.recipebook.android.data.auth.FirebaseAuthRepositoryImpl
import com.recipebook.android.data.repository.CommentRepositoryImpl
import com.recipebook.android.data.repository.FavoriteRepositoryImpl
import com.recipebook.android.data.repository.IngredientRepositoryImpl
import com.recipebook.android.data.repository.MealPlanRepositoryImpl
import com.recipebook.android.data.repository.RatingRepositoryImpl
import com.recipebook.android.data.repository.RecipeRepositoryImpl
import com.recipebook.android.data.repository.SearchHistoryRepositoryImpl
import com.recipebook.android.data.repository.ShoppingListRepositoryImpl
import com.recipebook.android.data.repository.TagRepositoryImpl
import com.recipebook.android.data.repository.UserRepositoryImpl
import com.recipebook.android.domain.repository.AuthRepository
import com.recipebook.android.domain.repository.CommentRepository
import com.recipebook.android.domain.repository.FavoriteRepository
import com.recipebook.android.domain.repository.IngredientRepository
import com.recipebook.android.domain.repository.MealPlanRepository
import com.recipebook.android.domain.repository.RatingRepository
import com.recipebook.android.domain.repository.RecipeRepository
import com.recipebook.android.domain.repository.SearchHistoryRepository
import com.recipebook.android.domain.repository.ShoppingListRepository
import com.recipebook.android.domain.repository.TagRepository
import com.recipebook.android.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton
    fun provideAuthRepository(impl: FirebaseAuthRepositoryImpl): AuthRepository = impl

    @Provides @Singleton
    fun provideRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository = impl

    @Provides @Singleton
    fun provideFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository = impl

    @Provides @Singleton
    fun provideMealPlanRepository(impl: MealPlanRepositoryImpl): MealPlanRepository = impl

    @Provides @Singleton
    fun provideShoppingListRepository(impl: ShoppingListRepositoryImpl): ShoppingListRepository = impl

    @Provides @Singleton
    fun provideCommentRepository(impl: CommentRepositoryImpl): CommentRepository = impl

    @Provides @Singleton
    fun provideRatingRepository(impl: RatingRepositoryImpl): RatingRepository = impl

    @Provides @Singleton
    fun provideSearchHistoryRepository(impl: SearchHistoryRepositoryImpl): SearchHistoryRepository = impl

    @Provides @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides @Singleton
    fun provideTagRepository(impl: TagRepositoryImpl): TagRepository = impl

    @Provides @Singleton
    fun provideIngredientRepository(impl: IngredientRepositoryImpl): IngredientRepository = impl
}
