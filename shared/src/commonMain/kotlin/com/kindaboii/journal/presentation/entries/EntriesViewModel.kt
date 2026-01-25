package com.kindaboii.journal.presentation.entries

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kindaboii.journal.domain.usecase.GetEntriesUseCase

class EntriesViewModel(
    getEntriesUseCase: GetEntriesUseCase,
) {
    private val _uiState = mutableStateOf(EntriesUiState(entries = getEntriesUseCase()))
    val uiState: State<EntriesUiState> = _uiState
}
