package com.recipebook.android.domain.repository

import com.recipebook.android.domain.model.User
import com.recipebook.android.domain.util.Resource

interface UserRepository {
    suspend fun getCurrentUser(): Resource<User>
    suspend fun getUserById(id: String): Resource<User>
    suspend fun updateUser(user: User): Resource<Unit>
}
