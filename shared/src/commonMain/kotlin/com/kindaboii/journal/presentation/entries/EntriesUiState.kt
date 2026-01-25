package com.kindaboii.journal.presentation.entries

import com.kindaboii.journal.domain.model.Entry

data class EntriesUiState(
    val entries: List<Entry> = emptyList(),
)
