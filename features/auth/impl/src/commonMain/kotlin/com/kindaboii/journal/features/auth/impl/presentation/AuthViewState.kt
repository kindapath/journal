package com.kindaboii.journal.features.auth.impl.presentation

data class AuthViewState(
    val mode: AuthMode = AuthMode.SignIn,
    val email: String = "",
    val password: String = "",
    val confirmationCode: String = "",
    val isSubmitting: Boolean = false,
    val isConfirming: Boolean = false,
    val isResendingCode: Boolean = false,
    val pendingConfirmationEmail: String = "",
    val infoMessage: String? = null,
    val errorMessage: String? = null,
)