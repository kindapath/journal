package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.auth.api.AuthRepository
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
    private val authRepository: AuthRepository,
) {
    fun getEntries(): Flow<List<Entry>> = commonDataSource.getEntries()

    suspend fun getEntryById(id: String): Entry? = commonDataSource.getEntryById(id)

    suspend fun insertEntry(entry: Entry) {
        val userId = requireCurrentUserId()
        commonDataSource.insertEntry(entry.copy(userId = userId))
    }

    suspend fun updateEntry(entry: Entry) {
        val userId = requireCurrentUserId()
        commonDataSource.updateEntry(entry.copy(userId = userId))
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

    private fun requireCurrentUserId(): String =
        authRepository.currentUserId()
            ?: error("Operation requires authenticated user context")
}
