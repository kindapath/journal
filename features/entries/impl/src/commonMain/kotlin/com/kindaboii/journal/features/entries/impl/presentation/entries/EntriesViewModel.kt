package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.export.generateAllEntriesHtml
import com.kindaboii.journal.features.entries.impl.export.generateEntryHtml
import com.kindaboii.journal.features.entries.impl.export.printHtml
import kotlinx.coroutines.Dispatchers
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EntriesViewModel(
    private val repository: EntryRepository,
): ViewModel() {
    private val _viewState = MutableStateFlow<EntriesViewState>(EntriesViewState.Loading)
    val viewState: StateFlow<EntriesViewState> = _viewState.asStateFlow()

    private var allEntries: List<Entry> = emptyList()
    private var dateFilter = EntriesDateRangeFilter()

    init {
        viewModelScope.launch {
            repository.getEntries().collect { entries ->
                allEntries = entries
                updateViewState()
            }
        }
    }

    fun onApplyDateFilter(from: LocalDate?, to: LocalDate?) {
        dateFilter = normalizeDateFilter(EntriesDateRangeFilter(from = from, to = to))
        updateViewState()
    }

    fun onClearDateFilter() {
        dateFilter = EntriesDateRangeFilter()
        updateViewState()
    }

    fun onDeleteEntry(entryId: String) {
        viewModelScope.launch {
            repository.deleteEntryById(entryId)
        }
    }

    fun onExportEntry(entry: Entry) {
        viewModelScope.launch(Dispatchers.Default) {
            val html = generateEntryHtml(entry)
            printHtml(html, "Запись в дневнике. ${exportTimestamp()}")
        }
    }

    fun onExportAll() {
        if (allEntries.isEmpty()) return
        viewModelScope.launch(Dispatchers.Default) {
            val html = generateAllEntriesHtml(allEntries)
            printHtml(html, "Дневник. ${exportTimestamp()}")
        }
    }

    private fun updateViewState() {
        val filteredEntries = allEntries.filterByDateRange(dateFilter)
        _viewState.value = when {
            allEntries.isEmpty() -> EntriesViewState.Empty(
                dateFilter = dateFilter,
                isFiltered = false,
            )

            filteredEntries.isEmpty() -> EntriesViewState.Empty(
                dateFilter = dateFilter,
                isFiltered = dateFilter.isActive,
            )

            else -> EntriesViewState.Content(
                entries = filteredEntries,
                dateFilter = dateFilter,
            )
        }
    }

    private fun normalizeDateFilter(filter: EntriesDateRangeFilter): EntriesDateRangeFilter {
        val from = filter.from
        val to = filter.to
        return if (from != null && to != null && from > to) {
            EntriesDateRangeFilter(from = to, to = from)
        } else {
            filter
        }
    }

    private fun List<Entry>.filterByDateRange(filter: EntriesDateRangeFilter): List<Entry> {
        if (!filter.isActive) return this

        return filter { entry ->
            val createdDate = entry.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
            val matchesFrom = filter.from?.let { createdDate >= it } ?: true
            val matchesTo = filter.to?.let { createdDate <= it } ?: true
            matchesFrom && matchesTo
        }
    }

    private fun exportTimestamp(): String {
        val dt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val d = "${dt.day.toString().padStart(2, '0')}.${dt.monthNumber.toString().padStart(2, '0')}.${dt.year}"
        val t = "${dt.hour.toString().padStart(2, '0')}-${dt.minute.toString().padStart(2, '0')}-${dt.second.toString().padStart(2, '0')}"
        return "$d $t"
    }
}