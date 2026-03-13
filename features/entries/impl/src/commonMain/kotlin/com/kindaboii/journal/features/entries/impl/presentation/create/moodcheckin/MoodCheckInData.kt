package com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin

enum class MoodRange(val label: String) {
    VERY_UNPLEASANT("Очень неприятные"),
    UNPLEASANT("Неприятные"),
    SLIGHTLY_UNPLEASANT("Слегка неприятные"),
    NEUTRAL("Нейтральные"),
    SLIGHTLY_PLEASANT("Слегка приятные"),
    PLEASANT("Приятные"),
    VERY_PLEASANT("Очень приятные"),
}

data class InfluenceGroup(
    val items: List<String>,
)

private val unpleasantEmotions = listOf(
    "Тревога",
    "Раздражение",
    "Грусть",
    "Эмоциональная боль",
    "Злость",
    "Одиночество",
    "Усталость",
    "Подавленность",
    "Растерянность",
    "Обида",
    "Страх",
    "Неуверенность",
    "Вина",
    "Смущение",
    "Разочарование",
    "Опустошение",
    "Напряжение",
    "Стыд",
    "Беспомощность",
    "Уязвимость",
    "Паника",
)

private val neutralEmotions = listOf(
    "Удовлетворенность",
    "Спокойствие",
    "Умиротворенность",
    "Безразличие",
    "Уязвимость",
)

private val pleasantEmotions = listOf(
    "Радость",
    "Легкое волнение",
    "Смущение",
    "Вдохновение",
    "Интерес",
    "Надежда",
    "Нежность",
    "Гордость",
    "Благодарность",
    "Любовь",
    "Свобода",
    "Энергия",
    "Уверенность",
    "Воодушевление",
    "Удовлетворенность",
    "Спокойствие",
    "Умиротворенность",
)

val emotionsByMoodRange: Map<MoodRange, List<String>> = mapOf(
    MoodRange.VERY_UNPLEASANT to unpleasantEmotions,
    MoodRange.UNPLEASANT to unpleasantEmotions,
    MoodRange.SLIGHTLY_UNPLEASANT to unpleasantEmotions,
    MoodRange.NEUTRAL to neutralEmotions,
    MoodRange.SLIGHTLY_PLEASANT to pleasantEmotions,
    MoodRange.PLEASANT to pleasantEmotions,
    MoodRange.VERY_PLEASANT to pleasantEmotions,
)

val influenceGroups: List<InfluenceGroup> = listOf(
    InfluenceGroup(
        items = listOf(
            "Здоровье",
            "Фитнес",
            "Забота о себе",
            "Хобби и увлечения",
            "Личность и самоопределение",
            "Духовная жизнь",
        ),
    ),
    InfluenceGroup(
        items = listOf(
            "Сообщество",
            "Семья",
            "Друзья",
            "Партнер",
            "Свидания и личная жизнь",
        ),
    ),
    InfluenceGroup(
        items = listOf(
            "Задачи",
            "Работа",
            "Образование",
            "Путешествия",
            "Погода",
            "Текущие события",
            "Деньги",
        ),
    ),
)

fun moodValueToRange(value: Long): MoodRange = when {
    value <= 14 -> MoodRange.VERY_UNPLEASANT
    value <= 28 -> MoodRange.UNPLEASANT
    value <= 42 -> MoodRange.SLIGHTLY_UNPLEASANT
    value <= 57 -> MoodRange.NEUTRAL
    value <= 71 -> MoodRange.SLIGHTLY_PLEASANT
    value <= 85 -> MoodRange.PLEASANT
    else -> MoodRange.VERY_PLEASANT
}

fun moodRangeLabel(value: Long): String = moodValueToRange(value).label
