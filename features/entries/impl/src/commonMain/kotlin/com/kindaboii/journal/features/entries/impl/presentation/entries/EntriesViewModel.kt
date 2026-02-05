package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.SyncManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EntriesViewModel(
    private val repository: EntryRepository,
    private val syncManager: SyncManager, // TODO: ALWAYS should use repository and not direct access
): ViewModel() {
    private val _viewState = MutableStateFlow<EntriesViewState>(EntriesViewState.Loading)
    val viewState: StateFlow<EntriesViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            syncManager.startSync() // TODO: ALWAYS should use repository and not direct access
        }
    }


    fun onDeleteEntry(entryId: String) {
        viewModelScope.launch {
            repository.deleteEntryById(entryId)
        }
    }
}


