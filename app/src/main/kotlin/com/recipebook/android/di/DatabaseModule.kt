package com.recipebook.android.di

import android.content.Context
import androidx.room.Room
import com.recipebook.android.data.local.database.AppDatabase
import com.recipebook.android.data.local.database.dao.FavoriteCacheDao
import com.recipebook.android.data.local.database.dao.RecipeCacheDao
import com.recipebook.android.data.local.database.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "recipebook.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideRecipeCacheDao(db: AppDatabase): RecipeCacheDao = db.recipeCacheDao()

    @Provides
    @Singleton
    fun provideFavoriteCacheDao(db: AppDatabase): FavoriteCacheDao = db.favoriteCacheDao()

    @Provides
    @Singleton
    fun provideSearchHistoryDao(db: AppDatabase): SearchHistoryDao = db.searchHistoryDao()
}
