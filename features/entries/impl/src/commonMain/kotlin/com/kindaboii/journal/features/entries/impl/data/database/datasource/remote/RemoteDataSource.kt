package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getEntries(): Flow<List<Entry>>
    fun getEntryById(id: String): Flow<Entry?>
    suspend fun upsertEntry(entry: Entry): NetworkResult<Unit>
    suspend fun deleteEntryById(id: String): NetworkResult<Unit>
}
