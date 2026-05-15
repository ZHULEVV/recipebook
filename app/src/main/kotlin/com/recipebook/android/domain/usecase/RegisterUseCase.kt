package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.model.User
import com.recipebook.android.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, displayName: String): Result<User> =
        authRepository.register(email, password, displayName)
}
