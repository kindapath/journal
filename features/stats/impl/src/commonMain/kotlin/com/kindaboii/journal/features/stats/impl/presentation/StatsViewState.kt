package com.kindaboii.journal.features.stats.impl.presentation

import kotlinx.datetime.LocalDate

data class MoodPoint(val date: LocalDate, val value: Long)

sealed interface StatsViewState {
    data object Loading : StatsViewState
    data class Content(
        val totalEntries: Int,
        val totalWords: Int,
        val currentStreak: Int,
        val moodPoints: List<MoodPoint>,
    ) : StatsViewState
}
