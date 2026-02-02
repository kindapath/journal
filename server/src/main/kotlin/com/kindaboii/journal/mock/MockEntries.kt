package com.kindaboii.journal.mock

import kotlinx.serialization.Serializable

@Serializable
data class EntryResponse(
    val id: String,
    val title: String? = null,
    val body: String? = null,
    val mood: MoodResponse? = null,
    val createdAt: String,
    val updatedAt: String? = null,
)

@Serializable
data class MoodResponse(
    val value: Long,
    val emotions: List<String> = emptyList(),
    val influences: List<String> = emptyList(),
)

object FakeEntryRepository {
    fun getEntries(): List<EntryResponse> =
        listOf(
            EntryResponse(
                id = "1",
                title = "Morning notes",
                body = "Quiet start, coffee, and a short walk.",
                mood = MoodResponse(
                    value = 70,
                    emotions = listOf("calm", "grateful"),
                    influences = listOf("sleep", "coffee"),
                ),
                createdAt = "2024-04-01T09:15:00Z",
                updatedAt = null,
            ),
            EntryResponse(
                id = "2",
                title = "Afternoon",
                body = "Focused work session with good progress.",
                mood = MoodResponse(
                    value = 62,
                    emotions = listOf("focused"),
                    influences = listOf("work"),
                ),
                createdAt = "2024-04-01T14:40:00Z",
                updatedAt = null,
            ),
            EntryResponse(
                id = "3",
                title = null,
                body = "No title today, just a short reflection.",
                mood = null,
                createdAt = "2024-04-02T18:30:00Z",
                updatedAt = null,
            ),
        )

    fun getEntryById(id: String): EntryResponse? = getEntries().firstOrNull { it.id == id }
}
