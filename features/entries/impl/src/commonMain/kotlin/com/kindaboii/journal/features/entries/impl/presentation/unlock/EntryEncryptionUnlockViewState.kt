package com.kindaboii.journal.features.entries.impl.presentation.unlock

enum class EntryEncryptionUnlockMode {
    Create,
    Unlock,
}

data class EntryEncryptionUnlockViewState(
    val mode: EntryEncryptionUnlockMode? = null,
    val passphrase: String = "",
    val repeatedPassphrase: String = "",
    val dataLossRiskAccepted: Boolean = false,
    val rememberOnThisDevice: Boolean = true,
    val canRememberOnThisDevice: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val isLoading: Boolean
        get() = mode == null
}
