package com.kindaboii.journal.features.auth.api

sealed interface AuthState {
    data object Loading : AuthState

    data object Unauthenticated : AuthState

    data class Authenticated(
        val userId: String,
    ) : AuthState
}
