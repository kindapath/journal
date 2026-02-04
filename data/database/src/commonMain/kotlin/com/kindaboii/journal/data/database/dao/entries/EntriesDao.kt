package com.kindaboii.journal.data.database.dao.entries

import com.kindaboii.journal.features.entries.schema.Entries
import kotlinx.coroutines.flow.Flow

interface EntriesDao {
    fun getEntries(): Flow<List<Entries>>
    fun getAllEntries(): Flow<List<Entries>>
    fun getEntryById(id: String): Flow<Entries?>
    suspend fun insertEntry(entity: Entries)
    suspend fun updateEntry(entity: Entries)
    suspend fun deleteEntryById(id: String)
    suspend fun deleteAllEntries()
    suspend fun replaceAll(entries: List<Entries>)
}
