package com.kindaboii.journal.lifecycle.demo

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal fun demoEntries(nowMillis: Long = Clock.System.now().toEpochMilliseconds()): List<Entry> =
    demoEntrySeeds.map { seed -> seed.toEntry(nowMillis) }

internal fun hasCurrentDemoFeed(entries: List<Entry>): Boolean =
    entries
        .take(3)
        .map { it.title?.trim().orEmpty() } == currentTopDemoTitles

private val currentTopDemoTitles = listOf(
    "Спокойное завершение дня",
    "После хорошей встречи",
    "Тревожное утро",
)

private val demoEntrySeeds = listOf(
    // Последние дни: список записей, календарь и текущая серия.
    DemoEntrySeed(
        offsetMs = offset(hours = 2),
        title = "Спокойное завершение дня",
        body = "День прошёл без резких скачков. Я не чувствую особой радости или тяжести, скорее ровное состояние и ясность в мыслях.",
        mood = DemoMoodSeed(
            value = 54,
            emotions = listOf("Спокойствие", "Удовлетворенность"),
            influences = listOf("Забота о себе"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(hours = 6),
        title = "После хорошей встречи",
        body = "После разговора с близким человеком появилось ощущение тепла и внутренней опоры. Хочется сохранить это состояние и запомнить, что на меня так влияет.",
        mood = DemoMoodSeed(
            value = 88,
            emotions = listOf("Радость", "Благодарность", "Надежда"),
            influences = listOf("Друзья", "Сообщество"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(hours = 11),
        title = "Тревожное утро",
        body = "С утра было сложно собраться: мысли скачут, а список дел кажется слишком большим. Помогло хотя бы зафиксировать это состояние и не делать вид, что всё в порядке.",
        mood = DemoMoodSeed(
            value = 24,
            emotions = listOf("Тревога", "Напряжение"),
            influences = listOf("Задачи", "Образование"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 1, hours = 2),
        title = "После разговора с научруком",
        body = "После встречи стало спокойнее: задачи не исчезли, но появилась ясность, с чего начинать и что реально успеть до дедлайна.",
        mood = DemoMoodSeed(
            value = 86,
            emotions = listOf("Облегчение", "Надежда", "Уверенность"),
            influences = listOf("Образование", "Задачи"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 1, hours = 9),
        body = "Устал, но хотя бы не держу это в себе. Решил записать состояние без заголовка.",
        mood = DemoMoodSeed(
            value = 28,
            emotions = listOf("Усталость", "Напряжение"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 2, hours = 3),
        title = "Небольшой прогресс",
        mood = DemoMoodSeed(
            value = 72,
            influences = listOf("Работа", "Задачи"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 2, hours = 11),
        title = "Без эмоционального опроса",
        body = "Иногда я просто фиксирую мысль или событие и не прохожу шкалу состояния.",
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 3, hours = 2),
        body = "Выдохнул.",
        mood = DemoMoodSeed(
            value = 43,
            emotions = listOf("Спокойствие"),
            influences = listOf("Забота о себе"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 3, hours = 8),
        mood = DemoMoodSeed(
            value = 14,
            emotions = listOf("Тревога", "Беспомощность"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 4, hours = 4),
        mood = DemoMoodSeed(
            value = 15,
            influences = listOf("Текущие события"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 5, hours = 1),
        title = "Тяжёлый день в университете",
        body = "Накапливается много мелких задач, и от этого сложно почувствовать завершённость хотя бы в чём-то одном.",
        mood = DemoMoodSeed(
            value = 28,
            emotions = listOf("Раздражение"),
            influences = listOf("Образование", "Задачи"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 5, hours = 9),
        title = "Собрался и пошёл дальше",
        body = "Снаружи день не поменялся, но внутри стало легче, когда я разбил одну большую задачу на три маленьких шага.",
        mood = DemoMoodSeed(
            value = 29,
            influences = listOf("Работа", "Задачи", "Забота о себе"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 6, hours = 5),
        body = """
            У меня было ощущение, что я топчусь на месте, хотя на самом деле сделал больше, чем заметил в моменте.

            Когда пересмотрел список задач и закрыл хотя бы несколько небольших пунктов, состояние стало менее тяжёлым. Полезно видеть прогресс не только по большим результатам, но и по маленьким действиям.
        """.trimIndent(),
        mood = DemoMoodSeed(
            value = 42,
            emotions = listOf("Подавленность", "Растерянность"),
            influences = listOf("Работа"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 7, hours = 7),
        title = "Очень длинный заголовок для проверки того, как карточка выглядит при большом количестве текста в одной строке",
    ),

    // Граничные значения шкалы и разные комбинации эмоций/факторов.
    DemoEntrySeed(
        offsetMs = offset(days = 8, hours = 3),
        mood = DemoMoodSeed(
            value = 43,
            emotions = listOf("Интерес", "Спокойствие"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 9, hours = 6),
        title = "Нейтрально и без тегов",
        mood = DemoMoodSeed(value = 57),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 10, hours = 2),
        body = "Ничего особенно яркого не произошло, но день оказался устойчивее, чем ожидалось.",
        mood = DemoMoodSeed(
            value = 58,
            emotions = listOf("Удовлетворенность"),
            influences = listOf("Погода"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 11, hours = 5),
        title = "Просто запись без mood",
        body = "Нужно не забыть обсудить структуру презентации и заранее проверить экспорт.",
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 12, hours = 4),
        title = "Стабильно",
        mood = DemoMoodSeed(
            value = 71,
            influences = listOf("Семья"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 13, hours = 6),
        body = "Было приятно вернуться к задаче уже без внутреннего сопротивления.",
        mood = DemoMoodSeed(
            value = 72,
            emotions = listOf("Интерес", "Воодушевление"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 14, hours = 2),
        title = "Почти отличный день",
        body = """
            Сегодня получилось удерживать внимание на одной задаче дольше обычного.

            Я заметил, что мне сильно помогает короткая пауза перед началом работы: не открывать сразу всё подряд, а сначала понять, что именно хочу закончить в ближайший час.
        """.trimIndent(),
        mood = DemoMoodSeed(
            value = 85,
            emotions = listOf("Гордость", "Удовлетворенность"),
            influences = listOf("Работа", "Задачи"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 15, hours = 8),
        title = "Хороший вечер",
        body = "Мало текста, но много спокойствия.",
        mood = DemoMoodSeed(
            value = 86,
            emotions = listOf("Спокойствие", "Благодарность"),
            influences = listOf("Друзья"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 17, hours = 2),
        title = "Максимально приятное состояние",
        mood = DemoMoodSeed(
            value = 100,
            emotions = listOf("Радость", "Свобода", "Энергия"),
            influences = listOf("Хобби и увлечения", "Друзья", "Путешествия"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 18, hours = 6),
        title = "Очень тяжёлое состояние",
        body = "Иногда полезно увидеть в истории и такие дни, чтобы приложение показывало реальную динамику, а не только приятные записи.",
        mood = DemoMoodSeed(value = 0),
    ),

    // Дополнительные сценарии для списка, статистики и экспорта.
    DemoEntrySeed(
        offsetMs = offset(days = 20, hours = 3),
        title = "Есть настроение, но нет текста",
        mood = DemoMoodSeed(
            value = 63,
            emotions = listOf("Умиротворенность"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 21, hours = 7),
        body = "Текст без заголовка, но с указанными факторами. Полезно для проверки карточек и детализации.",
        mood = DemoMoodSeed(
            value = 36,
            influences = listOf("Деньги", "Работа", "Задачи"),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 23, hours = 3),
        title = "Подготовка к защите",
        body = """
            Составил черновик демонстрации и отдельно выписал, какие сценарии стоит показать комиссии.

            Хочется, чтобы в приложении были видны не только красивые экраны, но и реальные пользовательские случаи: короткие записи, пустые поля, эмоциональные чек-ины без выбранных факторов и длинные тексты, которые можно экспортировать.
        """.trimIndent(),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 24, hours = 5),
        title = "Обычная рабочая заметка",
        body = "Ничего особенного, просто спокойный день и короткая фиксация состояния.",
        mood = DemoMoodSeed(
            value = 50,
            emotions = emptyList(),
            influences = emptyList(),
        ),
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 27, hours = 4),
        body = "Коротко.",
    ),
    DemoEntrySeed(
        offsetMs = offset(days = 30, hours = 3),
        title = "Большая рефлексия по итогам месяца",
        body = """
            За последний месяц стало заметно, что регулярность помогает лучше замечать не только сильные эмоции, но и переходные состояния. Раньше я фиксировал запись только в очень хорошие или очень тяжёлые дни, а теперь вижу и обычные нейтральные моменты.

            Это важно, потому что именно на фоне обычных дней понятнее, что влияет на самочувствие: загруженность, недосып, общение, ощущение завершённости задач или, наоборот, постоянное переключение между ними.

            Для демонстрации это ещё и полезный кейс длинной записи: можно проверить, как текст выглядит в списке, как открывается полностью и как потом попадает в экспорт.
        """.trimIndent(),
        mood = DemoMoodSeed(
            value = 91,
            emotions = listOf("Благодарность", "Надежда", "Уверенность"),
            influences = listOf("Забота о себе", "Образование", "Сообщество"),
        ),
    ),
)

private data class DemoEntrySeed(
    val offsetMs: Long,
    val title: String? = null,
    val body: String? = null,
    val mood: DemoMoodSeed? = null,
)

private data class DemoMoodSeed(
    val value: Long,
    val emotions: List<String> = emptyList(),
    val influences: List<String> = emptyList(),
)

@OptIn(ExperimentalUuidApi::class)
private fun DemoEntrySeed.toEntry(nowMillis: Long): Entry =
    Entry(
        id = Uuid.random().toString(),
        userId = "",
        title = title,
        body = body,
        mood = mood?.toMood(),
        createdAt = Instant.fromEpochMilliseconds(nowMillis - offsetMs),
        updatedAt = null,
        deletedAt = null,
    )

private fun DemoMoodSeed.toMood(): Mood =
    Mood(
        value = value,
        emotions = emotions,
        influences = influences,
    )

private fun offset(days: Int = 0, hours: Int = 0, minutes: Int = 0): Long =
    days * DAY_MS + hours * HOUR_MS + minutes * MINUTE_MS

private const val MINUTE_MS = 60L * 1000L
private const val HOUR_MS = 60L * MINUTE_MS
private const val DAY_MS = 24L * HOUR_MS
