package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.entries.impl.data.database.datasource.LocalDataSource
import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.Flow

class EntryRepository(
    private val localDataSource: LocalDataSource,
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
        localDataSource.deleteEntryById(id)
    }

    suspend fun deleteAllEntries() {
        localDataSource.deleteAllEntries()
    }

    suspend fun replaceAll(entries: List<Entry>) {
        localDataSource.replaceAll(entries)
    }
}
