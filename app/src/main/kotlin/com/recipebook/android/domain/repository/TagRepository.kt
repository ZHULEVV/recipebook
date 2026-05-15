package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.Tag
import com.recipebook.android.domain.util.Resource

interface TagRepository {
    suspend fun getTags(): Resource<List<Tag>>
}
