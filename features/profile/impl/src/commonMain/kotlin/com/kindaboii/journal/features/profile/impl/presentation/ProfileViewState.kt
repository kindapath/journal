package com.kindaboii.journal.features.profile.impl.presentation

data class ProfileViewState(
    val email: String = "",
    val currentEmail: String = "",
    val isDemoMode: Boolean = false,
    val pendingEmail: String = "",
    val emailCode: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isUpdatingEmail: Boolean = false,
    val isConfirmingEmail: Boolean = false,
    val isUpdatingPassword: Boolean = false,
    val emailMessage: String? = null,
    val passwordMessage: String? = null,
)
