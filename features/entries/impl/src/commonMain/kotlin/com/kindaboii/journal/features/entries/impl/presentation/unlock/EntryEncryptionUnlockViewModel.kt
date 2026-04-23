package com.kindaboii.journal.features.entries.impl.presentation.unlock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EntryEncryptionUnlockViewModel(
    private val repository: EntryRepository,
) : ViewModel() {
    private val _viewState = MutableStateFlow(EntryEncryptionUnlockViewState())
    val viewState: StateFlow<EntryEncryptionUnlockViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.unlockWithRememberedCodePhrase()
                .onSuccess { wasUnlocked ->
                    if (wasUnlocked) return@launch
                }
                .onFailure {
                    repository.forgetCodePhrase()
                }

            _viewState.update { state ->
                state.copy(canRememberOnThisDevice = repository.canRememberCodePhrase())
            }

            repository.hasEncryptedEntriesFlow().collect { hasEncryptedEntries ->
                _viewState.update { state ->
                    val nextMode = when {
                        hasEncryptedEntries -> EntryEncryptionUnlockMode.Unlock
                        state.mode == null -> EntryEncryptionUnlockMode.Create
                        else -> state.mode
                    }

                    state.copy(
                        mode = nextMode,
                        repeatedPassphrase = if (hasEncryptedEntries) "" else state.repeatedPassphrase,
                        dataLossRiskAccepted = if (hasEncryptedEntries) false else state.dataLossRiskAccepted,
                        errorMessage = state.errorMessage,
                    )
                }
            }
        }
    }

    fun onPassphraseChange(value: String) {
        _viewState.update { state ->
            state.copy(
                passphrase = value,
                errorMessage = null,
            )
        }
    }

    fun onRepeatedPassphraseChange(value: String) {
        _viewState.update { state ->
            state.copy(
                repeatedPassphrase = value,
                errorMessage = null,
            )
        }
    }

    fun onDataLossRiskAcceptedChange(value: Boolean) {
        _viewState.update { state ->
            state.copy(
                dataLossRiskAccepted = value,
                errorMessage = null,
            )
        }
    }

    fun onRememberOnThisDeviceChange(value: Boolean) {
        _viewState.update { state ->
            state.copy(rememberOnThisDevice = value)
        }
    }

    fun submit() {
        val state = _viewState.value
        val passphrase = state.passphrase

        if (passphrase.isBlank()) {
            _viewState.update { state ->
                state.copy(errorMessage = "Введите кодовую фразу.")
            }
            return
        }

        if (state.mode == EntryEncryptionUnlockMode.Create && passphrase != state.repeatedPassphrase) {
            _viewState.update { state ->
                state.copy(errorMessage = "Кодовые фразы не совпадают.")
            }
            return
        }

        if (state.mode == EntryEncryptionUnlockMode.Create && !state.dataLossRiskAccepted) {
            _viewState.update { state ->
                state.copy(errorMessage = "Подтвердите, что понимаете риск потери доступа без кодовой фразы.")
            }
            return
        }

        viewModelScope.launch {
            _viewState.update { state ->
                state.copy(
                    isSubmitting = true,
                    errorMessage = null,
                )
            }

            repository.unlockEncryption(passphrase)
                .onSuccess {
                    if (state.canRememberOnThisDevice && state.rememberOnThisDevice) {
                        repository.rememberCodePhrase(passphrase)
                    } else if (state.canRememberOnThisDevice) {
                        repository.forgetCodePhrase()
                    }

                    _viewState.update { state ->
                        state.copy(
                            passphrase = "",
                            repeatedPassphrase = "",
                            dataLossRiskAccepted = false,
                            isSubmitting = false,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure {
                    val hasEncryptedEntries = repository.hasEncryptedEntries()
                    _viewState.update { state ->
                        if (hasEncryptedEntries && state.mode == EntryEncryptionUnlockMode.Create) {
                            state.copy(
                                mode = EntryEncryptionUnlockMode.Unlock,
                                repeatedPassphrase = "",
                                dataLossRiskAccepted = false,
                                isSubmitting = false,
                            )
                        } else {
                            state.copy(
                                isSubmitting = false,
                                errorMessage = "Неверная кодовая фраза.",
                            )
                        }
                    }
                }
        }
    }

    private companion object {
        const val ENCRYPTED_ENTRIES_FOUND_MESSAGE =
            "Мы нашли зашифрованные записи. Введите кодовую фразу, которую вы уже создавали."
    }
}
