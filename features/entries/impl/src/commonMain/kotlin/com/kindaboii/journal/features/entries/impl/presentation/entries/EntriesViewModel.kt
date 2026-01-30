package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.impl.domain.usecase.GetEntriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EntriesViewModel(
    private val getEntriesUseCase: GetEntriesUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow<EntriesUiState>(EntriesUiState.Empty)
    val uiState: StateFlow<EntriesUiState> = _uiState.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            getEntriesUseCase().collectLatest { entries ->
                _uiState.value = when {
                    entries.isEmpty() -> EntriesUiState.Empty
                    else -> EntriesUiState.Content(entries)
                }
            }
        }
    }
}


