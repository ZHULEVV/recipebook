package com.recipebook.android.data.repository

import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.Tag
import com.recipebook.android.domain.repository.TagRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi
) : TagRepository {

    override suspend fun getTags(): Resource<List<Tag>> =
        safeApiCall { api.getTags().map { it.toDomain() } }
}
