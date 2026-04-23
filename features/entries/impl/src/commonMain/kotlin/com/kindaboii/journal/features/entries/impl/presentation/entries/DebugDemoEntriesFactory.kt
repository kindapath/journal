package com.kindaboii.journal.features.entries.impl.presentation.entries

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal object DebugDemoEntriesFactory {
    private val positiveCopies = listOf(
        DemoCopy(
            title = "Поймал спокойный ритм",
            shortBody = "День оказался на удивление ровным. Без суеты сделал главное и почувствовал устойчивость.",
            longBody = "С утра казалось, что снова начнется гонка, но этого не случилось. Я спокойно разобрал приоритеты, закрыл несколько важных задач и даже осталось немного воздуха между делами. Под конец дня поймал редкое чувство: не просто пережил день, а действительно прожил его в своем темпе.",
        ),
        DemoCopy(
            title = "Хороший разговор",
            shortBody = "Обычный диалог неожиданно поддержал. После него стало теплее и спокойнее.",
            longBody = "Сегодня очень помог короткий, но честный разговор. Без лишних советов сверху, без драматизации, просто нормальное человеческое присутствие. После него стало заметно легче держать фокус и меньше накручивать себя по поводу того, что все идет не так быстро, как хочется.",
        ),
        DemoCopy(
            title = "Немного горжусь собой",
            shortBody = "Закрыл задачу, которую все откладывал. Приятно видеть конкретный результат.",
            longBody = "Есть маленькая, но важная победа: сделал то, что давно откладывал. Самое ценное даже не в результате, а в том, что получилось не убежать от неприятного старта. После этого появилось ощущение опоры и уважения к себе.",
        ),
        DemoCopy(
            title = "Вечер получился живым",
            shortBody = "Удалось выйти из режима автопилота. Было больше интереса и энергии, чем обычно.",
            longBody = "К вечеру появилось ощущение живости. Не грандиозный подъем, но все вокруг перестало быть плоским. Поймал интерес к простым вещам, вернул себе чуть больше присутствия и ушел спать без внутреннего грохота в голове.",
        ),
        DemoCopy(
            title = "Получилось замедлиться",
            shortBody = "Сегодня особенно помогло никуда не бежать. Замедление пошло на пользу.",
            longBody = "Вместо привычного импульса ускориться я несколько раз сознательно притормозил. Это неожиданно сильно повлияло на весь день. Меньше ошибок, меньше внутренней жесткости и больше ощущения, что я управляю собой, а не только реагирую.",
        ),
        DemoCopy(
            title = "Тихая благодарность",
            shortBody = "Ничего сверхъестественного, просто хороший и нормальный день. И это приятно.",
            longBody = "Сегодня не случилось ничего героического, и в этом как раз была ценность. Хороший, обычный день без эмоциональных качелей уже сам по себе подарок. Поймал тихую благодарность за рабочий ритм, пару нормальных разговоров и возможность остаться в контакте с собой.",
        ),
        DemoCopy(
            title = "Собрался и сделал",
            shortBody = "Сложнее всего было начать, потом все пошло легче. Рад, что не слился на старте.",
            longBody = "Перед началом было много сопротивления и желание отвлечься на что угодно. Но после первого шага все начало собираться в понятную траекторию. В такие моменты особенно видно, как много меняет не вдохновение, а готовность просто начать.",
        ),
        DemoCopy(
            title = "Появилась надежда",
            shortBody = "Не все решено, но стало видно, куда двигаться дальше.",
            longBody = "На несколько часов словно рассеялся туман. Проблемы никуда не делись, но вместо тотальной неопределенности появилась следующая понятная точка. Этого оказалось достаточно, чтобы напряжение спало и вернулась надежда.",
        ),
    )

    private val neutralCopies = listOf(
        DemoCopy(
            title = "Обычный рабочий день",
            shortBody = "Без резких всплесков. Сделал свои дела и пошел дальше.",
            longBody = "Сегодняшний день нельзя назвать ни тяжелым, ни особенно приятным. Просто нормальный набор задач, несколько встреч и немного бытовых мелочей. Эмоционально довольно ровно, без сильных сдвигов в плюс или минус.",
        ),
        DemoCopy(
            title = "Фон скорее ровный",
            shortBody = "Настроение без ярких оттенков. Больше наблюдал, чем переживал что-то интенсивное.",
            longBody = "Внутри не было ни большого подъема, ни явного провала. Скорее фоновая собранность с редкими вспышками усталости. Такой день сложнее описывать, но он тоже важен: не каждый опыт должен быть драматичным, чтобы быть настоящим.",
        ),
        DemoCopy(
            title = "Между задачами и бытовухой",
            shortBody = "Много мелочей, которые не запомнятся, но все равно заняли день.",
            longBody = "Основное впечатление от дня - череда небольших дел и переключений. Вроде бы не было ничего тяжелого, но и радости особой тоже не случилось. Хорошее напоминание о том, что большая часть жизни состоит именно из таких промежуточных дней.",
        ),
        DemoCopy(
            title = "Спокойно, но чуть пусто",
            shortBody = "Внешне все нормально, внутри чуть меньше включенности, чем хотелось бы.",
            longBody = "День прошел спокойно и без лишнего напряжения, но местами чувствовалась пустота. Не болезненная, скорее как легкая дистанция от происходящего. Важно замечать и такие состояния, потому что они легко маскируются под обычную усталость.",
        ),
        DemoCopy(
            title = "Без перекосов",
            shortBody = "Хорошо, что не было эмоциональных качелей. Просто нейтральный отрезок.",
            longBody = "Иногда лучший итог дня - отсутствие резких качелей. Сегодня именно так. Не было ни поводов сильно радоваться, ни причин проваливаться глубже. Просто устойчивый нейтральный участок, который дал нервной системе немного перевести дух.",
        ),
        DemoCopy(
            title = "Тихий день",
            shortBody = "Меньше событий, меньше шума, меньше реакции. Все спокойно.",
            longBody = "Сегодня было заметно меньше внешнего шума. Из-за этого лучше слышно себя, но и скрыться за суетой не получается. В итоге день получился тихим, чуть отстраненным, но в целом нормальным и вполне переносимым.",
        ),
        DemoCopy(
            title = "Середина шкалы",
            shortBody = "Не плохо и не особенно хорошо. Просто день, который прошел.",
            longBody = "Иногда настроение ощущается буквально как середина шкалы. Без всплесков, без сильных интерпретаций и без желания драматизировать. Такой день не хочется особенно запоминать, но он дает полезный контраст по отношению к более ярким состояниям.",
        ),
        DemoCopy(
            title = "Ничего выдающегося",
            shortBody = "День не запомнится, но в этом тоже есть своя нормальность.",
            longBody = "Сегодня все шло без особых историй. Работа, короткие разговоры, пара мелких дел и немного времени на себя. Нейтральные дни не всегда вдохновляют, но именно они часто помогают почувствовать устойчивость.",
        ),
    )

    private val negativeCopies = listOf(
        DemoCopy(
            title = "Слишком много шума внутри",
            shortBody = "Даже простые задачи давались тяжелее обычного. Было много внутреннего напряжения.",
            longBody = "Сегодня почти весь день тянулось ощущение внутреннего шума. Ничего катастрофического не произошло, но фон был такой, будто организм все время готовится к плохим новостям. Из-за этого даже простые вещи отнимали больше сил, чем должны были.",
        ),
        DemoCopy(
            title = "Раздражение на пустом месте",
            shortBody = "Поймал себя на том, что слишком резко реагирую на мелочи.",
            longBody = "День получился вязким. Больше всего раздражало даже не происходящее, а собственная реакция на него. Неприятно замечать, как мелкие вещи выбивают сильнее, чем заслуживают, но это тоже важный сигнал про накопившееся напряжение.",
        ),
        DemoCopy(
            title = "Сил было мало",
            shortBody = "К середине дня заметно просел. Все давалось через усилие.",
            longBody = "К середине дня стало резко меньше энергии. Даже обычные действия воспринимались как что-то тяжелое и не по размеру. В такие моменты сложно понять, что именно болит: усталость, тревога или ощущение, что на меня всего слишком много.",
        ),
        DemoCopy(
            title = "Много самокритики",
            shortBody = "Сегодня особенно жестко звучал внутренний голос. Это отравляло весь фон.",
            longBody = "Самокритика сегодня была особенно громкой. Даже там, где я объективно справлялся, внутри звучало, что этого недостаточно. Такой фон подтачивает и сильно мешает видеть реальный масштаб вещей.",
        ),
        DemoCopy(
            title = "Чувствовал себя потерянно",
            shortBody = "Было трудно понять, с чего начать и куда вообще двигаться дальше.",
            longBody = "Ощущение потерянности не отпускало почти весь день. Не хватало не столько информации, сколько внутренней опоры, чтобы выбрать направление и двигаться без постоянных сомнений. Из-за этого даже небольшие решения давались с лишним усилием.",
        ),
        DemoCopy(
            title = "День тянул вниз",
            shortBody = "Ничего критичного, но фон был тяжелый и вязкий.",
            longBody = "Не было большого провала, но весь день ощущался вязким и тянущим вниз. Даже редкие хорошие моменты быстро растворялись на этом фоне. Под конец захотелось просто остановиться и перестать что-либо тащить на себе.",
        ),
        DemoCopy(
            title = "Не хватило опоры",
            shortBody = "Особенно остро чувствовались одиночество и нехватка поддержки.",
            longBody = "Бывает, что внешне все еще держится, а внутри уже очень не хватает опоры. Сегодня как раз такой случай. Особенно чувствовалось одиночество в тех местах, где обычно хочется простого подтверждения: тебя слышат, ты не один, с тобой не все бессмысленно.",
        ),
        DemoCopy(
            title = "Слишком много тревоги",
            shortBody = "Мысли крутились по кругу и не давали нормально переключиться.",
            longBody = "Тревога сегодня была не в форме паники, а скорее в виде постоянного фона, который мешал расслабиться даже на короткое время. Из-за этого было сложно удерживать внимание и еще сложнее - верить, что я вообще с чем-то справляюсь.",
        ),
    )

    private val tones = listOf(
        DemoTone.Positive,
        DemoTone.Neutral,
        DemoTone.Negative,
        DemoTone.Positive,
        DemoTone.Negative,
        DemoTone.Neutral,
        DemoTone.Positive,
        DemoTone.Positive,
        DemoTone.Neutral,
        DemoTone.Negative,
        DemoTone.Positive,
        DemoTone.Neutral,
        DemoTone.Negative,
        DemoTone.Positive,
        DemoTone.Neutral,
        DemoTone.Negative,
        DemoTone.Positive,
        DemoTone.Positive,
        DemoTone.Negative,
        DemoTone.Neutral,
        DemoTone.Positive,
        DemoTone.Negative,
        DemoTone.Neutral,
        DemoTone.Positive,
        DemoTone.Neutral,
        DemoTone.Negative,
        DemoTone.Positive,
        DemoTone.Negative,
        DemoTone.Neutral,
        DemoTone.Positive,
        DemoTone.Neutral,
        DemoTone.Negative,
        DemoTone.Positive,
        DemoTone.Neutral,
        DemoTone.Positive,
        DemoTone.Negative,
        DemoTone.Neutral,
        DemoTone.Positive,
        DemoTone.Negative,
        DemoTone.Neutral,
    )

    private val formats = listOf(
        DemoFormat.FullLong,
        DemoFormat.TitleAndMood,
        DemoFormat.BodyAndMood,
        DemoFormat.FullShort,
        DemoFormat.MoodOnly,
        DemoFormat.FullNoMood,
        DemoFormat.BodyOnly,
        DemoFormat.TitleOnly,
        DemoFormat.FullLong,
        DemoFormat.BodyAndMood,
        DemoFormat.FullShort,
        DemoFormat.MoodOnly,
        DemoFormat.FullNoMood,
        DemoFormat.TitleAndMood,
        DemoFormat.BodyOnly,
        DemoFormat.FullLong,
        DemoFormat.FullShort,
        DemoFormat.FullNoMood,
        DemoFormat.MoodOnly,
        DemoFormat.BodyAndMood,
        DemoFormat.FullLong,
        DemoFormat.TitleOnly,
        DemoFormat.FullShort,
        DemoFormat.BodyOnly,
        DemoFormat.FullNoMood,
        DemoFormat.TitleAndMood,
        DemoFormat.FullLong,
        DemoFormat.BodyAndMood,
        DemoFormat.MoodOnly,
        DemoFormat.FullShort,
        DemoFormat.BodyOnly,
        DemoFormat.FullNoMood,
        DemoFormat.TitleAndMood,
        DemoFormat.FullLong,
        DemoFormat.TitleOnly,
        DemoFormat.MoodOnly,
        DemoFormat.BodyAndMood,
        DemoFormat.FullShort,
        DemoFormat.FullNoMood,
        DemoFormat.BodyOnly,
    )

    private val positiveEmotions = listOf(
        listOf("Радость", "Благодарность"),
        listOf("Спокойствие", "Удовлетворенность"),
        listOf("Воодушевление", "Интерес"),
        listOf("Надежда", "Уверенность"),
        listOf("Нежность", "Любовь"),
        listOf("Свобода", "Энергия"),
    )

    private val neutralEmotions = listOf(
        listOf("Спокойствие"),
        listOf("Удовлетворенность"),
        listOf("Безразличие"),
        listOf("Умиротворенность"),
    )

    private val negativeEmotions = listOf(
        listOf("Тревога", "Напряжение"),
        listOf("Грусть", "Усталость"),
        listOf("Раздражение", "Обида"),
        listOf("Неуверенность", "Растерянность"),
        listOf("Одиночество", "Подавленность"),
        listOf("Разочарование", "Страх"),
    )

    private val positiveInfluences = listOf(
        listOf("Друзья", "Хобби и увлечения"),
        listOf("Партнер", "Забота о себе"),
        listOf("Фитнес", "Здоровье"),
        listOf("Работа", "Задачи"),
        listOf("Сообщество", "Семья"),
        listOf("Путешествия", "Хобби и увлечения"),
    )

    private val neutralInfluences = listOf(
        listOf("Работа"),
        listOf("Задачи", "Погода"),
        listOf("Образование"),
        listOf("Деньги"),
    )

    private val negativeInfluences = listOf(
        listOf("Работа", "Задачи"),
        listOf("Деньги", "Текущие события"),
        listOf("Партнер", "Семья"),
        listOf("Здоровье"),
        listOf("Погода", "Друзья"),
        listOf("Образование", "Работа"),
    )

    @OptIn(ExperimentalUuidApi::class)
    fun build(now: Instant): List<Entry> {
        val copiesByTone = mapOf(
            DemoTone.Positive to positiveCopies,
            DemoTone.Neutral to neutralCopies,
            DemoTone.Negative to negativeCopies,
        )
        val nextCopyIndex = mutableMapOf(
            DemoTone.Positive to 0,
            DemoTone.Neutral to 0,
            DemoTone.Negative to 0,
        )
        val entriesPerDay = mutableMapOf<Int, Int>()

        return buildDayOffsets().mapIndexed { index, dayOffset ->
            val tone = tones[index]
            val format = formats[index]
            val copies = requireNotNull(copiesByTone[tone])
            val copyIndex = nextCopyIndex[tone] ?: 0
            nextCopyIndex[tone] = copyIndex + 1
            val copy = copies[copyIndex % copies.size]
            val occurrence = entriesPerDay[dayOffset] ?: 0
            entriesPerDay[dayOffset] = occurrence + 1

            Entry(
                id = Uuid.random().toString(),
                userId = "",
                title = titleFor(format, copy),
                body = bodyFor(format, copy, index),
                mood = moodFor(format, tone, index),
                createdAt = buildCreatedAt(now, dayOffset, occurrence),
                updatedAt = null,
                deletedAt = null,
            )
        }
    }

    private fun buildDayOffsets(): List<Int> {
        val duplicatedDays = setOf(6, 14, 23, 31)
        val offsets = mutableListOf<Int>()
        for (dayOffset in 0 until 36) {
            offsets += dayOffset
            if (dayOffset in duplicatedDays) {
                offsets += dayOffset
            }
        }
        return offsets
    }

    private fun buildCreatedAt(
        now: Instant,
        dayOffset: Int,
        occurrence: Int,
    ): Instant {
        val primaryHours = listOf(2, 5, 8, 11, 14, 4, 7)
        val primaryMinutes = listOf(12, 34, 18, 47, 9, 26, 41)
        val secondaryHours = listOf(17, 19, 20, 16)
        val secondaryMinutes = listOf(22, 8, 39, 55)

        val hourOffset = if (occurrence == 0) {
            primaryHours[dayOffset % primaryHours.size]
        } else {
            secondaryHours[dayOffset % secondaryHours.size]
        }
        val minuteOffset = if (occurrence == 0) {
            primaryMinutes[dayOffset % primaryMinutes.size]
        } else {
            secondaryMinutes[dayOffset % secondaryMinutes.size]
        }
        val ageMillis = dayOffset * MILLIS_PER_DAY +
            hourOffset * MILLIS_PER_HOUR +
            minuteOffset * MILLIS_PER_MINUTE

        return Instant.fromEpochMilliseconds(now.toEpochMilliseconds() - ageMillis)
    }

    private fun titleFor(
        format: DemoFormat,
        copy: DemoCopy,
    ): String? = when (format) {
        DemoFormat.BodyOnly,
        DemoFormat.BodyAndMood,
        DemoFormat.MoodOnly,
        -> null

        else -> copy.title
    }

    private fun bodyFor(
        format: DemoFormat,
        copy: DemoCopy,
        index: Int,
    ): String? = when (format) {
        DemoFormat.TitleOnly,
        DemoFormat.TitleAndMood,
        DemoFormat.MoodOnly,
        -> null

        DemoFormat.FullLong -> copy.longBody
        DemoFormat.FullShort -> copy.shortBody
        DemoFormat.FullNoMood -> if (index % 2 == 0) copy.longBody else copy.shortBody
        DemoFormat.BodyOnly -> if (index % 3 == 0) copy.longBody else copy.shortBody
        DemoFormat.BodyAndMood -> if (index % 2 == 0) copy.shortBody else copy.longBody
    }

    private fun moodFor(
        format: DemoFormat,
        tone: DemoTone,
        index: Int,
    ): Mood? {
        if (format == DemoFormat.FullNoMood || format == DemoFormat.TitleOnly || format == DemoFormat.BodyOnly) {
            return null
        }

        val value = when (tone) {
            DemoTone.Positive -> listOf(69L, 76L, 84L, 92L)[index % 4]
            DemoTone.Neutral -> listOf(46L, 51L, 55L, 57L)[index % 4]
            DemoTone.Negative -> listOf(12L, 24L, 31L, 38L)[index % 4]
        }
        val emotions = when (tone) {
            DemoTone.Positive -> positiveEmotions[index % positiveEmotions.size]
            DemoTone.Neutral -> neutralEmotions[index % neutralEmotions.size]
            DemoTone.Negative -> negativeEmotions[index % negativeEmotions.size]
        }
        val influences = when (tone) {
            DemoTone.Positive -> positiveInfluences[index % positiveInfluences.size]
            DemoTone.Neutral -> neutralInfluences[index % neutralInfluences.size]
            DemoTone.Negative -> negativeInfluences[index % negativeInfluences.size]
        }

        return Mood(
            value = value,
            emotions = emotions,
            influences = influences,
        )
    }

    private data class DemoCopy(
        val title: String,
        val shortBody: String,
        val longBody: String,
    )

    private enum class DemoTone {
        Positive,
        Neutral,
        Negative,
    }

    private enum class DemoFormat {
        FullLong,
        FullShort,
        TitleOnly,
        BodyOnly,
        MoodOnly,
        FullNoMood,
        TitleAndMood,
        BodyAndMood,
    }

    private const val MILLIS_PER_MINUTE = 60_000L
    private const val MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE
    private const val MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR
}
