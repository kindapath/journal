package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kindaboii.journal.features.entries.impl.domain.usecase.GetEntriesUseCase

class EntriesViewModel(
    private val getEntriesUseCase: GetEntriesUseCase,
): ViewModel() {
    private val _uiState = mutableStateOf<EntriesUiState>(EntriesUiState.Empty)
    val uiState: State<EntriesUiState> = _uiState

    init {
        loadEntries()
    }

    private fun loadEntries() {
        val entries = getEntriesUseCase()
        _uiState.value = when {
            entries.isEmpty() -> EntriesUiState.Empty
            else -> EntriesUiState.Content(entries)
        }
    }
}


