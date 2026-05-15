package com.recipebook.android.domain.usecase

import com.recipebook.android.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> =
        authRepository.resetPassword(email)
}
