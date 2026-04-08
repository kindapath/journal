package com.kindaboii.journal.features.stats.impl.presentation

import kotlinx.datetime.LocalDate

data class StatsDateRangeFilter(
    val from: LocalDate? = null,
    val to: LocalDate? = null,
) {
    val isActive: Boolean
        get() = from != null || to != null
}

data class MoodPoint(val date: LocalDate, val value: Long)

sealed interface StatsViewState {
    data object Loading : StatsViewState
    data class Content(
        val totalEntries: Int,
        val totalWords: Int,
        val currentStreak: Int,
        val moodPoints: List<MoodPoint>,
        val dateFilter: StatsDateRangeFilter,
        val chartStartDate: LocalDate,
        val chartEndDate: LocalDate,
    ) : StatsViewState
}
