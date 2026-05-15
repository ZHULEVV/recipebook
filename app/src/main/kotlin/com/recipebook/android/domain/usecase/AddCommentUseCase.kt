package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.Comment
import com.recipebook.android.domain.repository.CommentRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(recipeId: String, text: String): Resource<Comment> =
        commentRepository.addComment(recipeId, text)
}
