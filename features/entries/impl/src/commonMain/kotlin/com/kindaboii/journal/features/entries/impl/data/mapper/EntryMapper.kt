package com.kindaboii.journal.features.entries.impl.data.mapper



import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import com.kindaboii.journal.features.entries.impl.data.datasource.remote.models.EntryDto
import com.kindaboii.journal.features.entries.schema.Entries
import kotlin.time.Instant

fun Entries.toModel() = Entry(
    id = id,
    userId = user_id,
    title = title,
    body = body,
    mood = mood_value?.let {
        Mood(
            value = it,
            emotions = mood_emotions ?: emptyList(),
            influences = mood_influences ?: emptyList()
        )
    },
    createdAt = Instant.parse(created_at.replace(" ", "T")),
    updatedAt = updated_at?.let { Instant.parse(it.replace(" ", "T")) },
    deletedAt = deleted_at?.let { Instant.parse(it.replace(" ", "T")) }
)

fun Entry.toEntity() = Entries(
    id = id,
    user_id = userId,
    title = title,
    body = body,
    mood_value = mood?.value,
    mood_emotions = mood?.emotions,
    mood_influences = mood?.influences,
    created_at = createdAt.toString(),
    updated_at = updatedAt?.toString(),
    deleted_at = deletedAt?.toString(),
)



fun Entry.toDto() = EntryDto(
    id = id,
    userId = userId,
    title = title,
    body = body,
    moodValue = mood?.value,
    moodEmotions = mood?.emotions ?: emptyList(),
    moodInfluences = mood?.influences ?: emptyList(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt?.toString(),
    deletedAt = deletedAt?.toString(),
)
