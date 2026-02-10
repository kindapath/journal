package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EntriesViewModel(
    private val repository: EntryRepository,
): ViewModel() {
    private val _viewState = MutableStateFlow<EntriesViewState>(EntriesViewState.Loading)
    val viewState: StateFlow<EntriesViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getEntries().collect { entries ->
                _viewState.value = if (entries.isEmpty()) {
                    EntriesViewState.Empty
                } else {
                    EntriesViewState.Content(entries)
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


