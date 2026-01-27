package com.kindaboii.journal.features.entries.impl.presentation.entries

import com.kindaboii.journal.features.entries.impl.domain.model.Entry

sealed interface EntriesUiState {
    data object Empty : EntriesUiState
    data class Content(val entries: List<Entry>) : EntriesUiState
}


