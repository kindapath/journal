package com.kindaboii.journal.data.repository

import com.kindaboii.journal.domain.model.Entry
import kotlinx.datetime.LocalDate

class FakeEntryRepository : EntryRepository {
    override fun getEntries(): List<Entry> = listOf(
        Entry(
            id = "1",
            title = "Путешествие, день 3",
            summary = "Я в восторге от природы. В этой поездке хочу замедлиться и замечать детали.",
            date = LocalDate(2024, 4, 1),
        ),
        Entry(
            id = "2",
            title = "Утренний рынок",
            summary = "Нашли маленький фермерский рынок. Было тихо, мягко и очень уютно.",
            date = LocalDate(2024, 3, 31),
        ),
        Entry(
            id = "3",
            title = "Вечерние мысли",
            summary = "Сидел у окна и слушал дождь. День был спокойным, и это было хорошо.",
            date = LocalDate(2024, 3, 30),
        ),
        Entry(
            id = "4",
            title = "Путешествие, день 3",
            summary = "Я в восторге от природы. В этой поездке хочу замедлиться и замечать детали.",
            date = LocalDate(2024, 4, 1),
        ),
        Entry(
            id = "5",
            title = "Утренний рынок",
            summary = "Нашли маленький фермерский рынок. Было тихо, мягко и очень уютно.",
            date = LocalDate(2024, 3, 31),
        ),
        Entry(
            id = "6",
            title = "Вечерние мысли",
            summary = "Сидел у окна и слушал дождь. День был спокойным, и это было хорошо.",
            date = LocalDate(2024, 3, 30),
        ),
        Entry(
            id = "7",
            title = "Путешествие, день 3",
            summary = "Я в восторге от природы. В этой поездке хочу замедлиться и замечать детали.",
            date = LocalDate(2024, 4, 1),
        ),
        Entry(
            id = "8",
            title = "Утренний рынок",
            summary = "Нашли маленький фермерский рынок. Было тихо, мягко и очень уютно.",
            date = LocalDate(2024, 3, 31),
        ),
        Entry(
            id = "9",
            title = "Вечерние мысли",
            summary = "Сидел у окна и слушал дождь. День был спокойным, и это было хорошо.",
            date = LocalDate(2024, 3, 30),
        ),
        Entry(
            id = "10",
            title = "Путешествие, день 3",
            summary = "Я в восторге от природы. В этой поездке хочу замедлиться и замечать детали.",
            date = LocalDate(2024, 4, 1),
        ),
        Entry(
            id = "11",
            title = "Утренний рынок",
            summary = "Нашли маленький фермерский рынок. Было тихо, мягко и очень уютно.",
            date = LocalDate(2024, 3, 31),
        ),
        Entry(
            id = "12",
            title = "Вечерние мысли",
            summary = "Сидел у окна и слушал дождь. День был спокойным, и это было хорошо.",
            date = LocalDate(2024, 3, 30),
        ),

    )
}
