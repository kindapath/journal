package com.kindaboii.journal.features.stats.impl.presentation

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class StatsCalculatorTest {

    private val tz = TimeZone.UTC
    private val today = LocalDate(2026, 3, 18)
    private val userId = "550e8400-e29b-41d4-a716-446655440000"

    private fun date(day: Int) = LocalDate(2026, 3, day)

    private fun mood(value: Long) = Mood(value = value, emotions = emptyList(), influences = emptyList())

    private fun entryAt(
        date: LocalDate,
        body: String? = null,
        mood: Mood? = null,
    ): Entry = Entry(
        id = date.toString(),
        userId = userId,
        title = null,
        body = body,
        mood = mood,
        createdAt = LocalDateTime(date.year, date.month, date.day, 12, 0)
            .toInstant(tz),
        updatedAt = null,
        deletedAt = null,
    )

    // --- computeStreak ---

    // Записи за 3 дня подряд (включая сегодня) дают серию длиной 3
    @Test
    fun streakConsecutiveDaysIncludingToday() {
        val entries = listOf(
            entryAt(date(16)),
            entryAt(date(17)),
            entryAt(date(18)),
        )
        assertEquals(3, StatsCalculator.computeStreak(entries, today, tz))
    }

    // Пропуск одного дня обрывает серию — считаются только дни после разрыва
    @Test
    fun streakGapBreaksTheChain() {
        val entries = listOf(
            entryAt(date(15)),
            // gap on 3/16
            entryAt(date(17)),
            entryAt(date(18)),
        )
        assertEquals(2, StatsCalculator.computeStreak(entries, today, tz))
    }

    // Если сегодня записи нет, серия считается от вчерашнего дня назад
    @Test
    fun streakNoEntryTodayCountsFromYesterday() {
        val entries = listOf(
            entryAt(date(16)),
            entryAt(date(17)),
        )
        assertEquals(2, StatsCalculator.computeStreak(entries, today, tz))
    }

    // Нет записей ни сегодня, ни вчера — серия равна 0
    @Test
    fun streakNoEntryTodayOrYesterdayReturnsZero() {
        val entries = listOf(
            entryAt(date(15)),
            entryAt(date(16)),
        )
        assertEquals(0, StatsCalculator.computeStreak(entries, today, tz))
    }

    // Несколько записей в один день не увеличивают серию — день считается один раз
    @Test
    fun streakMultipleEntriesSameDayCountAsOne() {
        val entries = listOf(entryAt(today), entryAt(today), entryAt(today))
        assertEquals(1, StatsCalculator.computeStreak(entries, today, tz))
    }

    // --- computeMoodPoints ---

    // Записи без указанного настроения не попадают в результат
    @Test
    fun moodPointsEntriesWithoutMoodAreFilteredOut() {
        val entries = listOf(entryAt(today, body = "no mood"))
        assertEquals(emptyList(), StatsCalculator.computeMoodPoints(entries, today, tz))
    }

    // Записи старше 30 дней отбрасываются, остальные сортируются по дате с сохранением значений
    @Test
    fun moodPointsFiltersByWindowAndSortsByDate() {
        val entries = listOf(
            entryAt(date(15), mood = mood(3)),
            entryAt(LocalDate(2026, 2, 1), mood = mood(4)), // older than 30 days
            entryAt(date(10), mood = mood(5)),
        )
        val result = StatsCalculator.computeMoodPoints(entries, today, tz)

        assertEquals(listOf(date(10), date(15)), result.map { it.date })
        assertEquals(listOf(5L, 3L), result.map { it.value })
    }

    // --- computeStats ---

    // Пустой список записей — все метрики равны нулю
    @Test
    fun computeStatsEmptyEntriesReturnZeroes() {
        val result = StatsCalculator.computeStats(emptyList(), today, tz)
        assertEquals(0, result.totalEntries)
        assertEquals(0, result.totalWords)
        assertEquals(0, result.currentStreak)
        assertEquals(emptyList(), result.moodPoints)
    }

    // Подсчёт слов: null, пустые и пробельные тела записей дают 0 слов
    @Test
    fun computeStatsCountsWordsIgnoresNullAndBlankBodies() {
        val entries = listOf(
            entryAt(today, body = "Hello world"),
            entryAt(today, body = "One two three four"),
            entryAt(today, body = null),
            entryAt(today, body = "   "),
        )
        val result = StatsCalculator.computeStats(entries, today, tz)
        assertEquals(4, result.totalEntries)
        assertEquals(6, result.totalWords)
    }

    // Интеграционный тест: 3 записи подряд с текстом и настроением — проверяет все метрики вместе
    @Test
    fun computeStatsFullScenarioWithStreakAndMood() {
        val entries = listOf(
            entryAt(date(16), body = "Day one entry", mood = mood(4)),
            entryAt(date(17), body = "Day two"),
            entryAt(date(18), body = "Today is great", mood = mood(4)),
        )
        val result = StatsCalculator.computeStats(entries, today, tz)
        assertEquals(3, result.totalEntries)
        assertEquals(8, result.totalWords)
        assertEquals(3, result.currentStreak)
        assertEquals(2, result.moodPoints.size)
    }
}
