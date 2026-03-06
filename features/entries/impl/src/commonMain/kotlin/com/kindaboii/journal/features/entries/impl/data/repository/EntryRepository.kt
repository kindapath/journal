package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.auth.api.AuthRepository
import com.kindaboii.journal.features.auth.api.AuthState
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.datasource.common.CommonEntriesDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Instant

/**
 * Repository for entry management.
 * Implements domain logic like soft-delete pattern.
 * Platform-agnostic - relies on EntriesDataSource abstraction.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class EntryRepository(
    private val commonDataSource: CommonEntriesDataSource,
    private val authRepository: AuthRepository,
) {
    fun getEntries(): Flow<List<Entry>> =
        authRepository.authState.flatMapLatest { authState ->
            when (authState) {
                is AuthState.Authenticated -> commonDataSource.getEntries(authState.userId)
                AuthState.Loading,
                AuthState.Unauthenticated,
                -> flowOf(emptyList())
            }
        }

    suspend fun getEntryById(id: String): Entry? =
        commonDataSource.getEntryById(
            id = id,
            userId = requireCurrentUserId(),
        )

    suspend fun insertEntry(entry: Entry) {
        val userId = requireCurrentUserId()
        commonDataSource.insertEntry(entry.copy(userId = userId))
    }

    suspend fun updateEntry(entry: Entry) {
        val userId = requireCurrentUserId()
        commonDataSource.updateEntry(entry.copy(userId = userId))
    }

    suspend fun deleteEntryById(id: String) {
        val existing = getEntryById(id) ?: return
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
