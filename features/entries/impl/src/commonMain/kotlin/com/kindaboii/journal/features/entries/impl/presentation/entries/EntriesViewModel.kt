package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kindaboii.journal.features.entries.impl.domain.usecase.GetEntriesUseCase

class EntriesViewModel(
    getEntriesUseCase: GetEntriesUseCase,
) {
    private val _uiState = mutableStateOf(
        getEntriesUseCase().let { entries ->
            if (entries.isEmpty()) EntriesUiState.Empty else EntriesUiState.Content(entries)
        },
    )
    val uiState: State<EntriesUiState> = _uiState
}


