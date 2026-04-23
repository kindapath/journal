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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class EntriesViewModel(
    private val repository: EntryRepository,
): ViewModel() {
    private val _viewState = MutableStateFlow<EntriesViewState>(EntriesViewState.Loading)
    val viewState: StateFlow<EntriesViewState> = _viewState.asStateFlow()
    private val _isGeneratingDemoData = MutableStateFlow(false)
    val isGeneratingDemoData: StateFlow<Boolean> = _isGeneratingDemoData.asStateFlow()

    private var allEntries: List<Entry> = emptyList()
    private var dateFilter = EntriesDateRangeFilter()
    private var searchFilter = EntriesSearchFilter()

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

    fun onSearchQueryChange(query: String) {
        searchFilter = EntriesSearchFilter(query = query)
        updateViewState()
    }

    fun onClearSearch() {
        searchFilter = EntriesSearchFilter()
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

    fun onGenerateDemoData() {
        if (_isGeneratingDemoData.value) return

        viewModelScope.launch {
            _isGeneratingDemoData.value = true
            try {
                DebugDemoEntriesFactory.build(now = Clock.System.now())
                    .sortedBy(Entry::createdAt)
                    .forEach { entry -> repository.insertEntry(entry) }
            } finally {
                _isGeneratingDemoData.value = false
            }
        }
    }

    private fun updateViewState() {
        val filteredEntries = allEntries
            .filterByDateRange(dateFilter)
            .filterBySearch(searchFilter)

        _viewState.value = when {
            allEntries.isEmpty() -> EntriesViewState.Empty(
                dateFilter = dateFilter,
                searchFilter = searchFilter,
            )

            filteredEntries.isEmpty() -> EntriesViewState.Empty(
                dateFilter = dateFilter,
                searchFilter = searchFilter,
            )

            else -> EntriesViewState.Content(
                entries = filteredEntries,
                dateFilter = dateFilter,
                searchFilter = searchFilter,
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

    private fun List<Entry>.filterBySearch(filter: EntriesSearchFilter): List<Entry> {
        if (!filter.isActive) return this

        val normalizedQuery = filter.query
            .trim()
            .lowercase()
        val terms = normalizedQuery
            .split(Regex("\\s+"))
            .filter(String::isNotBlank)

        if (terms.isEmpty()) return this

        return filter { entry ->
            val searchableText = buildString {
                entry.title?.trim()?.takeIf(String::isNotEmpty)?.let {
                    append(it)
                    append('\n')
                }
                entry.body?.trim()?.takeIf(String::isNotEmpty)?.let(::append)
            }.lowercase()

            terms.all(searchableText::contains)
        }
    }

    private fun exportTimestamp(): String {
        val dt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val d = "${dt.day.toString().padStart(2, '0')}.${dt.monthNumber.toString().padStart(2, '0')}.${dt.year}"
        val t = "${dt.hour.toString().padStart(2, '0')}-${dt.minute.toString().padStart(2, '0')}-${dt.second.toString().padStart(2, '0')}"
        return "$d $t"
    }
}
