package com.kindaboii.journal.features.entries.impl.data.database.datasource.local

import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getEntries(): Flow<List<Entry>>
    fun getEntryById(id: String): Flow<Entry?>
    suspend fun insertEntry(entry: Entry)
    suspend fun updateEntry(entry: Entry)
}
