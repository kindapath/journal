package com.kindaboii.journal.features.auth.impl.data

import com.kindaboii.journal.features.auth.api.AuthRepository
import com.kindaboii.journal.features.auth.api.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SupabaseAuthRepository(
    private val supabase: SupabaseClient,
) : AuthRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val authState: StateFlow<AuthState> =
        supabase.auth.sessionStatus
            .map(::toAuthState)
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = AuthState.Loading,
            )

    override suspend fun signUp(
        email: String,
        password: String,
    ): Result<Unit> = runCatching {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signIn(
        email: String,
        password: String,
    ): Result<Unit> = runCatching {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        supabase.auth.signOut()
    }

    override fun currentAccessToken(): String? =
        supabase.auth.currentSessionOrNull()?.accessToken

    override fun currentUserId(): String? =
        supabase.auth.currentSessionOrNull()?.user?.id

    private fun toAuthState(sessionStatus: SessionStatus): AuthState =
        when (sessionStatus) {
            SessionStatus.Initializing -> AuthState.Loading
            is SessionStatus.Authenticated -> {
                val userId = sessionStatus.session.user?.id.orEmpty()
                if (userId.isBlank()) {
                    AuthState.Unauthenticated
                } else {
                    AuthState.Authenticated(userId = userId)
                }
            }

            is SessionStatus.NotAuthenticated,
            is SessionStatus.RefreshFailure,
            -> AuthState.Unauthenticated
        }
}
