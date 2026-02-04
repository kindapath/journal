package com.kindaboii.journal.mock

import kotlinx.serialization.Serializable

@Serializable
data class EntriesResponse(
    val entries: List<EntryResponse>,
)

@Serializable
data class EntryResponse(
    val id: String,
    val title: String? = null,
    val body: String? = null,
    val mood: MoodResponse? = null,
    val createdAt: String,
    val updatedAt: String? = null,
    val deletedAt: String? = null,
)

@Serializable
data class MoodResponse(
    val value: Long,
    val emotions: List<String> = emptyList(),
    val influences: List<String> = emptyList(),
)

object FakeEntryRepository {
    private val entries = mutableMapOf<String, EntryResponse>(
        "1" to EntryResponse(
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
            deletedAt = null,
        ),
        "2" to EntryResponse(
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
            deletedAt = null,
        ),
        "3" to EntryResponse(
            id = "3",
            title = null,
            body = "No title today, just a short reflection.",
            mood = null,
            createdAt = "2024-04-02T18:30:00Z",
            updatedAt = null,
            deletedAt = null,
        ),
    )

    fun getEntries(): EntriesResponse =
        EntriesResponse(entries = entries.values.sortedByDescending { it.createdAt })

    fun getEntryById(id: String): EntryResponse? = entries[id]

    fun upsertEntry(entry: EntryResponse): EntryResponse {
        entries[entry.id] = entry
        return entry
    }

    fun deleteEntryById(id: String): Boolean = entries.remove(id) != null
}
