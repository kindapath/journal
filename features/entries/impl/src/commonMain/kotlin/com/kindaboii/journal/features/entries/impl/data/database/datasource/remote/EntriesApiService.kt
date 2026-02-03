package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote

import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models.EntriesResponse
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models.EntryDto
import com.kindaboii.journal.network.NetworkResult
import io.ktor.client.HttpClient

interface EntriesApiService {
    suspend fun getEntries(): NetworkResult<EntriesResponse>
    suspend fun getEntryById(id: String): NetworkResult<EntryDto>
    suspend fun upsertEntry(entry: EntryDto): NetworkResult<Unit>
    suspend fun deleteEntryById(id: String): NetworkResult<Unit>
}

class EntriesApiServiceImpl(
    private val httpClient: HttpClient,
) : EntriesApiService {
    override suspend fun getEntries(): NetworkResult<EntriesResponse> =
        NetworkResult.Failure("Not implemented. Connect Supabase.")

    override suspend fun getEntryById(id: String): NetworkResult<EntryDto> =
        NetworkResult.Failure("Not implemented. Connect Supabase.")

    override suspend fun upsertEntry(entry: EntryDto): NetworkResult<Unit> =
        NetworkResult.Failure("Not implemented. Connect Supabase.")

    override suspend fun deleteEntryById(id: String): NetworkResult<Unit> =
        NetworkResult.Failure("Not implemented. Connect Supabase.")
}
