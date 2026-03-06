package com.kindaboii.journal.data.database.dao.entries

import com.kindaboii.journal.features.entries.schema.Entries
import kotlinx.coroutines.flow.Flow

interface EntriesDao {
    fun getEntries(userId: String): Flow<List<Entries>>
    suspend fun getEntryById(id: String, userId: String): Entries?
    suspend fun insertEntry(entity: Entries)
    suspend fun updateEntry(entity: Entries)
}
