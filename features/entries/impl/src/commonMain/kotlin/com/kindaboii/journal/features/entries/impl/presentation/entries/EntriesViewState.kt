package com.kindaboii.journal.features.entries.impl.presentation.entries

import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.datetime.LocalDate

data class EntriesDateRangeFilter(
    val from: LocalDate? = null,
    val to: LocalDate? = null,
) {
    val isActive: Boolean
        get() = from != null || to != null
}

sealed interface EntriesViewState {
    data object Loading : EntriesViewState

    data class Empty(
        val dateFilter: EntriesDateRangeFilter = EntriesDateRangeFilter(),
        val isFiltered: Boolean = false,
    ) : EntriesViewState

    data class Content(
        val entries: List<Entry>,
        val dateFilter: EntriesDateRangeFilter = EntriesDateRangeFilter(),
    ) : EntriesViewState
}