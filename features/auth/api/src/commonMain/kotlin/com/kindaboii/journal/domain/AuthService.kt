package com.kindaboii.journal.domain

import kotlinx.coroutines.flow.StateFlow

interface AuthService {
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
