package com.kindaboii.journal.features.entries.impl.presentation.entries

import com.kindaboii.journal.features.entries.impl.domain.model.Entry

data class EntriesUiState(
    val entries: List<Entry> = emptyList(),
)


