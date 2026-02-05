package com.kindaboii.journal.data.database.dao.entries

import com.kindaboii.journal.features.entries.schema.Entries
import kotlinx.coroutines.flow.Flow

interface EntriesDao {
    fun getEntries(): Flow<List<Entries>>
    fun getEntryById(id: String): Flow<Entries?>
    suspend fun insertEntry(entity: Entries)
    suspend fun updateEntry(entity: Entries)
}
