package com.recipebook.android.data.repository

import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.dto.AddCommentRequestDto
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.Comment
import com.recipebook.android.domain.repository.CommentRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi
) : CommentRepository {

    override suspend fun getComments(recipeId: String): Resource<List<Comment>> =
        safeApiCall { api.getComments(recipeId).map { it.toDomain() } }

    override suspend fun addComment(recipeId: String, text: String): Resource<Comment> =
        safeApiCall { api.addComment(recipeId, AddCommentRequestDto(text)).toDomain() }

    override suspend fun deleteComment(commentId: String): Resource<Unit> =
        safeApiCall { api.deleteComment(commentId) }
}
