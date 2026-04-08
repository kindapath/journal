package com.kindaboii.journal.features.stats.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.api.EntriesFeatureApi
import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class StatsViewModel(
    private val entriesFeatureApi: EntriesFeatureApi,
) : ViewModel() {

    private val _viewState = MutableStateFlow<StatsViewState>(StatsViewState.Loading)
    val viewState: StateFlow<StatsViewState> = _viewState.asStateFlow()

    private var allEntries: List<Entry> = emptyList()
    private var dateFilter = StatsDateRangeFilter()

    init {
        viewModelScope.launch {
            entriesFeatureApi.getEntries().collect { entries ->
                allEntries = entries
                updateViewState()
            }
        }
    }

    fun onApplyDateFilter(from: LocalDate?, to: LocalDate?) {
        dateFilter = normalizeDateFilter(StatsDateRangeFilter(from = from, to = to))
        updateViewState()
    }

    fun onClearDateFilter() {
        dateFilter = StatsDateRangeFilter()
        updateViewState()
    }

    private fun updateViewState() {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date
        val resolvedFilter = normalizeDateFilter(dateFilter)
        val chartEndDate = when {
            resolvedFilter.to != null -> resolvedFilter.to
            resolvedFilter.from != null && resolvedFilter.from > today -> resolvedFilter.from
            else -> today
        }
        val chartStartDate = when {
            resolvedFilter.from != null -> resolvedFilter.from
            resolvedFilter.to != null -> resolvedFilter.to.minus(DatePeriod(days = 29))
            else -> today.minus(DatePeriod(days = 29))
        }
        val filteredEntries = allEntries.filterByDateRange(resolvedFilter, tz)

        _viewState.value = StatsCalculator.computeStats(
            entries = filteredEntries,
            streakAnchorDate = chartEndDate,
            moodStartDate = chartStartDate,
            moodEndDate = chartEndDate,
            tz = tz,
            dateFilter = resolvedFilter,
        )
    }

    private fun normalizeDateFilter(filter: StatsDateRangeFilter): StatsDateRangeFilter {
        val from = filter.from
        val to = filter.to
        return if (from != null && to != null && from > to) {
            StatsDateRangeFilter(from = to, to = from)
        } else {
            filter
        }
    }

    private fun List<Entry>.filterByDateRange(
        filter: StatsDateRangeFilter,
        tz: TimeZone,
    ): List<Entry> {
        if (!filter.isActive) return this

        return filter { entry ->
            val createdDate = entry.createdAt.toLocalDateTime(tz).date
            val matchesFrom = filter.from?.let { createdDate >= it } ?: true
            val matchesTo = filter.to?.let { createdDate <= it } ?: true
            matchesFrom && matchesTo
        }
    }
}
