package com.kindaboii.journal.features.auth.api

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

interface AuthFeatureApi {
    val authState: StateFlow<AuthState>

    suspend fun signOut(): Result<Unit>

    @Composable
    fun AuthScreen()
}
