package com.recipebook.android.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.recipebook.android.domain.model.User
import com.recipebook.android.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomain())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override suspend fun login(email: String, password: String): Result<User> =
        runCatching {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.toDomain() ?: error("Пользователь не найден")
        }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): Result<User> = runCatching {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(
            userProfileChangeRequest { setDisplayName(displayName) }
        )?.await()
        result.user?.toDomain() ?: error("Пользователь не найден")
    }

    override suspend fun logout() = firebaseAuth.signOut()

    override suspend fun resetPassword(email: String): Result<Unit> =
        runCatching { firebaseAuth.sendPasswordResetEmail(email).await() }

    private fun FirebaseUser.toDomain() = User(
        id          = uid,
        email       = email ?: "",
        displayName = displayName,
        avatarUrl   = photoUrl?.toString()
    )
}
