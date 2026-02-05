package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.database.datasource.local.LocalDataSource
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.time.Instant

class EntryRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {
    fun getEntries(): Flow<List<Entry>> = localDataSource.getEntries()

    fun getEntryById(id: String): Flow<Entry?> = localDataSource.getEntryById(id)

    suspend fun insertEntry(entry: Entry) {
        localDataSource.insertEntry(entry)
    }

    suspend fun updateEntry(entry: Entry) {
        localDataSource.updateEntry(entry)
    }

    suspend fun deleteEntryById(id: String) {
        val existing = localDataSource.getEntryById(id).first() ?: return
        val deleted = existing.copy(
            deletedAt = nowInstant(),
            updatedAt = nowInstant(),
        )
        localDataSource.updateEntry(deleted)
    }

    private fun nowInstant(): Instant =
        Instant.fromEpochMilliseconds(kotlin.time.Clock.System.now().toEpochMilliseconds())
}
