package com.kindaboii.journal.features.auth.impl.data

import com.kindaboii.journal.domain.AuthEmailChangeResult
import com.kindaboii.journal.domain.AuthService
import com.kindaboii.journal.domain.AuthState
import com.kindaboii.journal.network.ApiConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
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

class SupabaseAuthService(
    private val supabase: SupabaseClient,
) : AuthService {
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

    override suspend fun signInDemo(): Result<Unit> = runCatching {
        check(ApiConfig.SUPABASE_ANON_AUTH_ENABLED) {
            "Demo access is not enabled for this build."
        }

        supabase.auth.signInAnonymously()
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        supabase.auth.signOut()
    }

    override suspend fun resendSignUpConfirmation(email: String): Result<Unit> = runCatching {
        supabase.auth.resendEmail(
            type = OtpType.Email.SIGNUP,
            email = email,
        )
    }

    override suspend fun verifySignUp(
        email: String,
        code: String,
    ): Result<Unit> = runCatching {
        supabase.auth.verifyEmailOtp(
            type = OtpType.Email.SIGNUP,
            email = email,
            token = code,
        )
        Unit
    }

    override suspend fun changeEmail(email: String): Result<AuthEmailChangeResult> = runCatching {
        val userInfo = supabase.auth.updateUser {
            this.email = email
        }

        AuthEmailChangeResult(
            currentEmail = userInfo.email,
            pendingEmail = userInfo.newEmail,
        )
    }

    override suspend fun verifyEmailChange(
        email: String,
        code: String,
    ): Result<String?> = runCatching {
        supabase.auth.verifyEmailOtp(
            type = OtpType.Email.EMAIL_CHANGE,
            email = email,
            token = code,
        )

        supabase.auth.retrieveUserForCurrentSession(updateSession = true).email
    }

    override suspend fun changePassword(password: String): Result<Unit> = runCatching {
        supabase.auth.updateUser {
            this.password = password
        }
        Unit
    }

    override fun currentAccessToken(): String? =
        supabase.auth.currentSessionOrNull()?.accessToken

    override fun currentUserId(): String? =
        supabase.auth.currentSessionOrNull()?.user?.id

    override fun currentUserEmail(): String? =
        supabase.auth.currentSessionOrNull()?.user?.email

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
