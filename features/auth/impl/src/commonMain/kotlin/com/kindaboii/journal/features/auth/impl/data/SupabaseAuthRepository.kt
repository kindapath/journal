package com.kindaboii.journal.features.auth.impl.data

import com.kindaboii.journal.features.auth.api.AuthRepository
import com.kindaboii.journal.features.auth.api.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SupabaseAuthRepository(
    private val supabase: SupabaseClient,
) : AuthRepository {
    private val _authState = MutableStateFlow(currentAuthState())
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    override suspend fun signUp(
        email: String,
        password: String,
    ): Result<Unit> = runCatching {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }.map {
        Unit
    }.onSuccess {
        _authState.value = currentAuthState()
    }

    override suspend fun signIn(
        email: String,
        password: String,
    ): Result<Unit> = runCatching {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }.map {
        Unit
    }.onSuccess {
        _authState.value = currentAuthState()
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        supabase.auth.signOut()
    }.onSuccess {
        _authState.value = AuthState.Unauthenticated
    }

    override fun currentAccessToken(): String? =
        supabase.auth.currentSessionOrNull()?.accessToken

    override fun currentUserId(): String? =
        supabase.auth.currentSessionOrNull()?.user?.id

    private fun currentAuthState(): AuthState {
        val userId = currentUserId().orEmpty()
        return if (userId.isBlank()) {
            AuthState.Unauthenticated
        } else {
            AuthState.Authenticated(userId = userId)
        }
    }
}
