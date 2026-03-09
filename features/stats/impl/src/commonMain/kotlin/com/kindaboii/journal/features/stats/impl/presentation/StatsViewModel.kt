package com.kindaboii.journal.features.stats.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.api.EntriesFeatureApi
import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class StatsViewModel(
    private val entriesFeatureApi: EntriesFeatureApi,
) : ViewModel() {

    private val _viewState = MutableStateFlow<StatsViewState>(StatsViewState.Loading)
    val viewState: StateFlow<StatsViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            entriesFeatureApi.getEntries().collect { entries ->
                _viewState.value = computeStats(entries)
            }
        }
    }

    private fun computeStats(entries: List<Entry>): StatsViewState.Content {
        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date

        val totalEntries = entries.size
        val totalWords = entries.sumOf { entry ->
            entry.body?.trim()
                ?.split(Regex("\\s+"))
                ?.count { it.isNotEmpty() }
                ?: 0
        }
        val currentStreak = computeStreak(entries, today, tz)
        val moodPoints = computeMoodPoints(entries, today, tz)

        return StatsViewState.Content(
            totalEntries = totalEntries,
            totalWords = totalWords,
            currentStreak = currentStreak,
            moodPoints = moodPoints,
        )
    }

    private fun computeStreak(
        entries: List<Entry>,
        today: LocalDate,
        tz: TimeZone,
    ): Int {
        val entryDates = entries
            .map { it.createdAt.toLocalDateTime(tz).date }
            .toSet()

        var streak = 0
        var current = today
        while (current in entryDates) {
            streak++
            current = current.minus(DatePeriod(days = 1))
        }
        // If today has no entry, check if a streak ended yesterday
        if (streak == 0) {
            current = today.minus(DatePeriod(days = 1))
            while (current in entryDates) {
                streak++
                current = current.minus(DatePeriod(days = 1))
            }
        }
        return streak
    }

    private fun computeMoodPoints(
        entries: List<Entry>,
        today: LocalDate,
        tz: TimeZone,
    ): List<MoodPoint> {
        val thirtyDaysAgo = today.minus(DatePeriod(days = 29))
        return entries
            .filter { it.mood != null }
            .map { entry ->
                MoodPoint(
                    date = entry.createdAt.toLocalDateTime(tz).date,
                    value = entry.mood!!.value,
                )
            }
            .filter { it.date >= thirtyDaysAgo && it.date <= today }
            .sortedBy { it.date }
    }
}
