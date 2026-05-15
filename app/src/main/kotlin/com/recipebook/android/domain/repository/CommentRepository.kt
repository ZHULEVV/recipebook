package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.Comment
import com.recipebook.android.domain.util.Resource

interface CommentRepository {
    suspend fun getComments(recipeId: String): Resource<List<Comment>>
    suspend fun addComment(recipeId: String, text: String): Resource<Comment>
    suspend fun deleteComment(commentId: String): Resource<Unit>
}
