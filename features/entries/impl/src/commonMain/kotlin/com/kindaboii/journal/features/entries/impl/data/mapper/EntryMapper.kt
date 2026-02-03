package com.kindaboii.journal.features.entries.impl.data.mapper



import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import com.kindaboii.journal.features.entries.schema.EntryEntity
import kotlin.time.Instant

fun EntryEntity.toModel() = Entry(
    id = id,
    title = title,
    body = body,
    mood = moodValue?.let {
        Mood(
            value = it,
            emotions = moodEmotions ?: emptyList(),
            influences = moodInfluences ?: emptyList()
        )
    },
    createdAt = Instant.parse(createdAt),
    updatedAt = updatedAt?.let { Instant.parse(it) },
    deletedAt = deletedAt?.let { Instant.parse(it) }
)

fun Entry.toEntity() = EntryEntity(
    id = id,
    title = title,
    body = body,
    moodValue = mood?.value,
    moodEmotions = mood?.emotions,
    moodInfluences = mood?.influences,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt?.toString(),
    deletedAt = deletedAt?.toString(),
)
