package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.export.generateAllEntriesHtml
import com.kindaboii.journal.features.entries.impl.export.generateEntryHtml
import com.kindaboii.journal.features.entries.impl.export.printHtml
import kotlinx.coroutines.Dispatchers
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

    fun onExportEntry(entry: Entry) {
        viewModelScope.launch(Dispatchers.Default) {
            val html = generateEntryHtml(entry)
            printHtml(html)
        }
    }

    fun onExportAll() {
        val entries = (_viewState.value as? EntriesViewState.Content)?.entries ?: return
        viewModelScope.launch(Dispatchers.Default) {
            val html = generateAllEntriesHtml(entries)
            printHtml(html)
        }
    }
}


