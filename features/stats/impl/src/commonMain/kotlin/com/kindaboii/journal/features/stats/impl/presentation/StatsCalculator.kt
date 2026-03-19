package com.kindaboii.journal.features.stats.impl.presentation

import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

internal object StatsCalculator {

    fun computeStats(
        entries: List<Entry>,
        today: LocalDate,
        tz: TimeZone,
    ): StatsViewState.Content {
        val totalEntries = entries.size
        val totalWords = entries.sumOf { entry ->
            entry.body?.trim()
                ?.split(' ', '\t', '\n', '\r')
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

    fun computeStreak(
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

    fun computeMoodPoints(
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
