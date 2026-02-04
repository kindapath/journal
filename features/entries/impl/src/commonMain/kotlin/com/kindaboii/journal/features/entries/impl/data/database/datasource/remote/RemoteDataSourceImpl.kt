package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models.toModel
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models.toDto
import com.kindaboii.journal.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSourceImpl(
    private val apiService: EntriesApiService,
) : RemoteDataSource {
    override fun getEntries(): Flow<List<Entry>> = flow {
        when (val result = apiService.getEntries()) {
            is NetworkResult.Success -> emit(
                result.data.entries.map { it.toModel() }
            )
            is NetworkResult.Failure -> error(result.errorMessage)
        }
    }

    override fun getEntryById(id: String): Flow<Entry?> = flow {
        when (val result = apiService.getEntryById(id)) {
            is NetworkResult.Success -> emit(result.data.toModel())
            is NetworkResult.Failure -> error(result.errorMessage)
        }
    }

    override suspend fun upsertEntry(entry: Entry): NetworkResult<Unit> =
        apiService.upsertEntry(entry.toDto())

    override suspend fun deleteEntryById(id: String): NetworkResult<Unit> =
        apiService.deleteEntryById(id)
}
