package com.kindaboii.journal.domain

data class AuthEmailChangeResult(
    val currentEmail: String?,
    val pendingEmail: String?,
)