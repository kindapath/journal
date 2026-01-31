package com.kindaboii.journal.data.database.dao.entries

import com.kindaboii.journal.features.entries.schema.EntryEntity
import kotlinx.coroutines.flow.Flow

interface EntriesDao {
    fun getEntries(): Flow<List<EntryEntity>>
    fun getEntryById(id: String): Flow<EntryEntity?>
    suspend fun insertEntry(entity: EntryEntity)
    suspend fun updateEntry(entity: EntryEntity)
    suspend fun deleteEntryById(id: String)
    suspend fun deleteAllEntries()
    suspend fun replaceAll(entries: List<EntryEntity>)
}
