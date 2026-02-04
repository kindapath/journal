package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntriesResponse(
    val entries: List<EntryDto> = emptyList(),
)

@Serializable
data class EntryDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String? = null,
    @SerialName("body")
    val body: String? = null,

    @SerialName("mood_value")
    val moodValue: Long? = null,

    @SerialName("mood_emotions")
    @Serializable(with = JsonStringListSerializer::class)
    val moodEmotions: List<String> = emptyList(),

    @SerialName("mood_influences")
    @Serializable(with = JsonStringListSerializer::class)
    val moodInfluences: List<String> = emptyList(),

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("deleted_at")
    val deletedAt: String? = null,
)

fun EntryDto.toModel() = Entry(
    id = id,
    title = title,
    body = body,
    mood = moodValue?.let {
        Mood(
            value = it,
            emotions = moodEmotions,
            influences = moodInfluences,
        )
    },
    createdAt = Instant.parse(createdAt),
    updatedAt = updatedAt?.let { Instant.parse(it) },
    deletedAt = deletedAt?.let { Instant.parse(it) },
)

fun Entry.toDto() = EntryDto(
    id = id,
    title = title,
    body = body,
    moodValue = mood?.value,
    moodEmotions = mood?.emotions ?: emptyList(),
    moodInfluences = mood?.influences ?: emptyList(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt?.toString(),
    deletedAt = deletedAt?.toString(),
)
