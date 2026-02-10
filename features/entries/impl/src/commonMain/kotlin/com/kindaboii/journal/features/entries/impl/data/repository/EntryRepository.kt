package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.datasource.common.CommonEntriesDataSource
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

/**
 * Repository for entry management.
 * Implements domain logic like soft-delete pattern.
 * Platform-agnostic - relies on EntriesDataSource abstraction.
 */
class EntryRepository(
    private val commonDataSource: CommonEntriesDataSource,
) {
    fun getEntries(): Flow<List<Entry>> = commonDataSource.getEntries()

    suspend fun getEntryById(id: String): Entry? = commonDataSource.getEntryById(id)

    suspend fun insertEntry(entry: Entry) {
        commonDataSource.insertEntry(entry)
    }

    suspend fun updateEntry(entry: Entry) {
        commonDataSource.updateEntry(entry)
    }

    suspend fun deleteEntryById(id: String) {
        val existing = commonDataSource.getEntryById(id) ?: return
        val deleted = existing.copy(
            deletedAt = nowInstant(),
            updatedAt = nowInstant(),
        )
        commonDataSource.updateEntry(deleted)
    }

    private fun nowInstant(): Instant =
        Instant.fromEpochMilliseconds(kotlin.time.Clock.System.now().toEpochMilliseconds())
}
