package com.kindaboii.journal.features.entries.impl.presentation.create

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateEntryViewModel {
    private val _uiState = MutableStateFlow(
        CreateEntryUiState(
            title = "",
            body = "",
        ),
    )
    val uiState: StateFlow<CreateEntryUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun onBodyChange(value: String) {
        _uiState.value = _uiState.value.copy(body = value)
    }
}


