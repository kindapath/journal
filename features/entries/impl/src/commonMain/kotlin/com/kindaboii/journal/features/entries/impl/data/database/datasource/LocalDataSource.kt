package com.kindaboii.journal.features.entries.impl.data.database.datasource

import com.kindaboii.journal.features.entries.impl.domain.models.Entry
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getEntries(): Flow<List<Entry>>
    fun getEntryById(id: String): Flow<Entry?>
    suspend fun insertEntry(entry: Entry)
    suspend fun updateEntry(entry: Entry)
    suspend fun deleteEntryById(id: String)
    suspend fun deleteAllEntries()
    suspend fun replaceAll(entries: List<Entry>)
}
