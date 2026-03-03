package com.kindaboii.journal.features.auth.impl

import androidx.compose.runtime.Composable
import com.kindaboii.journal.features.auth.api.AuthFeatureApi
import com.kindaboii.journal.features.auth.api.AuthRepository
import com.kindaboii.journal.features.auth.api.AuthState
import com.kindaboii.journal.features.auth.impl.presentation.AuthScreen as AuthScreenContent
import kotlinx.coroutines.flow.StateFlow

class AuthFeatureApiImpl(
    private val authRepository: AuthRepository,
) : AuthFeatureApi {
    override val authState: StateFlow<AuthState> = authRepository.authState

    override suspend fun signOut(): Result<Unit> = authRepository.signOut()

    @Composable
    override fun AuthScreen() {
        AuthScreenContent()
    }
}
