package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class EntriesResponse(
    val entries: List<EntryDto> = emptyList(),
)

@Serializable
data class EntryDto(
    val id: String,
    val title: String? = null,
    val body: String? = null,
    val mood: MoodDto? = null,
    val createdAt: String,
    val updatedAt: String? = null,
    val deletedAt: String? = null,
)

@Serializable
data class MoodDto(
    val value: Long,
    val emotions: List<String> = emptyList(),
    val influences: List<String> = emptyList(),
)

fun EntryDto.toModel() = Entry(
    id = id,
    title = title,
    body = body,
    mood = mood?.toModel(),
    createdAt = Instant.parse(createdAt),
    updatedAt = updatedAt?.let { Instant.parse(it) },
    deletedAt = deletedAt?.let { Instant.parse(it) },
)

fun MoodDto.toModel() = Mood(
    value = value,
    emotions = emotions,
    influences = influences,
)

fun Entry.toDto() = EntryDto(
    id = id,
    title = title,
    body = body,
    mood = mood?.toDto(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt?.toString(),
    deletedAt = deletedAt?.toString(),
)

fun Mood.toDto() = MoodDto(
    value = value,
    emotions = emotions,
    influences = influences,
)
