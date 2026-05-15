package com.recipebook.android.data.repository

import com.recipebook.android.data.remote.api.RecipeBookApi
import com.recipebook.android.data.remote.dto.UserDto
import com.recipebook.android.data.remote.mapper.toDomain
import com.recipebook.android.data.util.safeApiCall
import com.recipebook.android.domain.model.User
import com.recipebook.android.domain.repository.UserRepository
import com.recipebook.android.domain.util.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: RecipeBookApi
) : UserRepository {

    override suspend fun getCurrentUser(): Resource<User> =
        safeApiCall { api.getCurrentUser().toDomain() }

    override suspend fun getUserById(id: String): Resource<User> =
        safeApiCall { api.getUserById(id).toDomain() }

    override suspend fun updateUser(user: User): Resource<Unit> = safeApiCall {
        api.updateUser(UserDto(user.id, user.email, user.displayName, user.avatarUrl))
    }
}
