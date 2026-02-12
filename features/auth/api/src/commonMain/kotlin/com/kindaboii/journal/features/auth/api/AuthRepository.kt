package com.kindaboii.journal.features.auth.api

import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState>

    suspend fun signUp(
        email: String,
        password: String,
    ): Result<Unit>

    suspend fun signIn(
        email: String,
        password: String,
    ): Result<Unit>

    suspend fun signOut(): Result<Unit>

    fun currentAccessToken(): String?

    fun currentUserId(): String?
}
