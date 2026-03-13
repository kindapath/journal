package com.kindaboii.journal.domain

sealed interface AuthState {
    data object Loading : AuthState

    data object Unauthenticated : AuthState

    data class Authenticated(
        val userId: String,
    ) : AuthState
}
