package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.datasource.EntriesDataSource
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

/**
 * Repository for entry management.
 * Implements domain logic like soft-delete pattern.
 * Platform-agnostic - relies on EntriesDataSource abstraction.
 */
class EntryRepository(
    private val dataSource: EntriesDataSource,
) {
    fun getEntries(): Flow<List<Entry>> = dataSource.getEntries()

    suspend fun getEntryById(id: String): Entry? = dataSource.getEntryById(id)

    suspend fun insertEntry(entry: Entry) {
        dataSource.insertEntry(entry)
    }

    suspend fun updateEntry(entry: Entry) {
        dataSource.updateEntry(entry)
    }

    suspend fun deleteEntryById(id: String) {
        val existing = dataSource.getEntryById(id) ?: return
        val deleted = existing.copy(
            deletedAt = nowInstant(),
            updatedAt = nowInstant(),
        )
        dataSource.updateEntry(deleted)
    }

    private fun nowInstant(): Instant =
        Instant.fromEpochMilliseconds(kotlin.time.Clock.System.now().toEpochMilliseconds())
}
