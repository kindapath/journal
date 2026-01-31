package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.domain.usecase.GetEntriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EntriesViewModel(
    private val getEntriesUseCase: GetEntriesUseCase,
    private val repository: EntryRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<EntriesUiState>(EntriesUiState.Loading)
    val uiState: StateFlow<EntriesUiState> = _uiState.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            _uiState.value = EntriesUiState.Loading
            getEntriesUseCase()
            .collectLatest { entries ->
                _uiState.value = when {
                    entries.isEmpty() -> EntriesUiState.Empty
                    else -> EntriesUiState.Content(entries)
                }
            }
        }
    }

    fun onDeleteEntry(entryId: String) {
        viewModelScope.launch {
            repository.deleteEntryById(entryId)
        }
    }
}


