package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.Tag
import com.recipebook.android.domain.repository.TagRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(): Resource<List<Tag>> = tagRepository.getTags()
}
