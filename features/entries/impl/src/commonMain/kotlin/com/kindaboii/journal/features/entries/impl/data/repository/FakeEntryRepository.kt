package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.entries.impl.domain.model.Entry
import com.kindaboii.journal.features.entries.impl.domain.model.Mood
import kotlin.time.Instant

class FakeEntryRepository : EntryRepository {
    override fun getEntries(): List<Entry> =
        listOf(
            Entry(
                id = "1",
                title = "Путешествие, день 3",
                body = "Я в восторге от природы и того, как меняется свет в течение дня. В этой поездке хочу замедлиться, замечать детали и не торопить события. Мы шли по тропе вдоль озера, и каждый поворот открывал новый вид — будто страницы в дневнике. В такие моменты особенно ясно слышно тишину, и от этого она становится ещё глубже.",
                mood = Mood(
                    value = 78,
                    emotions = listOf("Благодарность", "Умиротворенность", "Тепло", "Спокойствие"),
                    influences = listOf("Забота о себе", "Прогулка", "Природа", "Фитнес"),
                ),
                createdAt = Instant.parse("2024-04-01T09:15:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "1a",
                title = null,
                body = "Сегодня почти без заголовка. Просто хочется зафиксировать пару мыслей и отпустить этот день.",
                mood = Mood(
                    value = 55,
                    emotions = listOf("Сдержанность", "Тишина", "Усталость"),
                    influences = listOf("Неспешный день", "Саморефлексия"),
                ),
                createdAt = Instant.parse("2024-04-02T18:30:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "2",
                title = "Утренний рынок",
                body = "Нашли маленький фермерский рынок — тихий, мягкий, почти камерный. Разговорились с продавцом сыра, он рассказал про свою деревню и про то, как встаёт рано, чтобы всё успеть. Я купил тёплый хлеб, и мы устроили маленький перекус прямо на скамейке. Такие простые моменты почему‑то остаются в памяти надолго.",
                mood = Mood(
                    value = 66,
                    emotions = listOf("Радость", "Уют"),
                    influences = listOf("Еда", "Утро", "Новый опыт", "Разговор"),
                ),
                createdAt = Instant.parse("2024-03-31T08:20:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "2a",
                title = "Заметка без текста",
                body = null,
                mood = Mood(
                    value = 40,
                    emotions = listOf("Пустота"),
                    influences = listOf("Пауза"),
                ),
                createdAt = Instant.parse("2024-03-31T21:05:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "3",
                title = "Вечерние мысли",
                body = "Сидел у окна и слушал дождь, который шёл мелкой, ровной стеной. День был спокойным, и это оказалось самым правильным его темпом. Я перечитал пару старых заметок и понял, как много в них простых радостей. Хотел бы чаще оставлять для себя такие тихие кусочки времени.",
                mood = Mood(
                    value = 62,
                    emotions = listOf("Спокойствие", "Мягкость"),
                    influences = listOf("Дождь", "Тишина", "Чтение"),
                ),
                createdAt = Instant.parse("2024-03-30T20:10:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "4",
                title = "Путешествие, день 3",
                body = "Я в восторге от природы. В этой поездке хочу замедлиться и замечать детали.",
                mood = Mood(
                    value = 74,
                    emotions = listOf("Вдохновение", "Радость", "Лёгкость"),
                    influences = listOf("Природа", "Дорога", "Свежий воздух"),
                ),
                createdAt = Instant.parse("2024-04-01T13:40:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "5",
                title = "Утренний рынок",
                body = "Нашли маленький фермерский рынок. Было тихо, мягко и очень уютно.",
                mood = Mood(
                    value = 60,
                    emotions = listOf("Уют", "Тепло"),
                    influences = listOf("Еда", "Люди"),
                ),
                createdAt = Instant.parse("2024-03-31T10:05:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "6",
                title = "Вечерние мысли",
                body = "Сидел у окна и слушал дождь. День был спокойным, и это было хорошо.",
                mood = Mood(
                    value = 58,
                    emotions = listOf("Тишина", "Ровность"),
                    influences = listOf("Дождь", "Окно", "Вечер"),
                ),
                createdAt = Instant.parse("2024-03-30T22:15:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "7",
                title = "Путешествие, день 3",
                body = "Я в восторге от природы. В этой поездке хочу замедлиться и замечать детали.",
                mood = Mood(
                    value = 71,
                    emotions = listOf("Восторг", "Вдохновение"),
                    influences = listOf("Прогулка", "Горы", "Солнце"),
                ),
                createdAt = Instant.parse("2024-04-01T07:30:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "8",
                title = "Утренний рынок",
                body = "Нашли маленький фермерский рынок. Было тихо, мягко и очень уютно.",
                mood = Mood(
                    value = 63,
                    emotions = listOf("Уют", "Спокойствие"),
                    influences = listOf("Еда", "Утро", "Теплый хлеб"),
                ),
                createdAt = Instant.parse("2024-03-31T09:50:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "9",
                title = "Вечерние мысли",
                body = "Сидел у окна и слушал дождь. День был спокойным, и это было хорошо.",
                mood = Mood(
                    value = 57,
                    emotions = listOf("Смирение"),
                    influences = listOf("Дождь", "Вечер"),
                ),
                createdAt = Instant.parse("2024-03-30T19:55:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "10",
                title = "Путешествие, день 3",
                body = "Я в восторге от природы. В этой поездке хочу замедлиться и замечать детали.",
                mood = Mood(
                    value = 76,
                    emotions = listOf("Радость", "Свобода"),
                    influences = listOf("Природа", "Дорога"),
                ),
                createdAt = Instant.parse("2024-04-01T11:05:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "11",
                title = "Утренний рынок",
                body = "Нашли маленький фермерский рынок. Было тихо, мягко и очень уютно.",
                mood = Mood(
                    value = 59,
                    emotions = listOf("Тепло"),
                    influences = listOf("Люди", "Еда", "Разговор"),
                ),
                createdAt = Instant.parse("2024-03-31T07:45:00Z"),
                updatedAt = null,
            ),
            Entry(
                id = "12",
                title = "Вечерние мысли",
                body = "Сидел у окна и слушал дождь. День был спокойным, и это было хорошо.",
                mood = Mood(
                    value = 54,
                    emotions = listOf("Спокойствие"),
                    influences = listOf("Дождь", "Тишина"),
                ),
                createdAt = Instant.parse("2024-03-30T18:40:00Z"),
                updatedAt = null,
            ),
        )
}
