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

    suspend fun resendSignUpConfirmation(email: String): Result<Unit>

    suspend fun verifySignUp(
        email: String,
        code: String,
    ): Result<Unit>

    suspend fun changeEmail(email: String): Result<AuthEmailChangeResult>

    suspend fun verifyEmailChange(
        email: String,
        code: String,
    ): Result<String?>

    suspend fun changePassword(password: String): Result<Unit>

    fun currentAccessToken(): String?

    fun currentUserId(): String?

    fun currentUserEmail(): String?
}