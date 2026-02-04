package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote

import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models.EntriesResponse
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models.EntryDto
import com.kindaboii.journal.network.ApiConfig
import com.kindaboii.journal.network.NetworkLogger
import com.kindaboii.journal.network.NetworkResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlin.time.TimeSource

interface EntriesApiService {
    suspend fun getEntries(): NetworkResult<EntriesResponse>
    suspend fun getEntryById(id: String): NetworkResult<EntryDto>
    suspend fun upsertEntry(entry: EntryDto): NetworkResult<Unit>
    suspend fun deleteEntryById(id: String): NetworkResult<Unit>
}

class EntriesApiServiceImpl(
    private val supabase: SupabaseClient,
    private val networkLogger: NetworkLogger,
) : EntriesApiService {
    private val tableName = "entries"
    private fun itemToSimpleString(item: EntryDto): String =
        "{id=${item.id}, title=${item.title}, createdAt=${item.createdAt}, updatedAt=${item.updatedAt}, deletedAt=${item.deletedAt}, moodValue=${item.moodValue}, moodEmotions=${item.moodEmotions}, moodInfluences=${item.moodInfluences}}"

    override suspend fun getEntries(): NetworkResult<EntriesResponse> =
        runCatching {
            val start = TimeSource.Monotonic.markNow()

            networkLogger.info("Supabase.getEntries start table=$tableName order=created_at desc")

            val items = supabase.from(tableName)
                .select {
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<EntryDto>()
            val durationMs = start.elapsedNow().inWholeMilliseconds
            networkLogger.info("Supabase.getEntries success count=${items.size} durationMs=$durationMs")

            if (ApiConfig.isDebug()) {
                val simple = items.joinToString(prefix = "[", postfix = "]", separator = ", ") {
                    itemToSimpleString(it)
                }
                networkLogger.debug("Supabase.getEntries items=$simple")
            }

            NetworkResult.Success(EntriesResponse(items))

        }.getOrElse { error ->
            networkLogger.error("Supabase.getEntries failed", error)
            NetworkResult.Failure(error.message ?: "Supabase error")
        }

    override suspend fun getEntryById(id: String): NetworkResult<EntryDto> =
        runCatching {
            val start = TimeSource.Monotonic.markNow()

            networkLogger.info("Supabase.getEntryById start table=$tableName id=$id")

            val item = supabase.from(tableName)
                .select {
                    filter { eq("id", id) }
                }
                .decodeSingle<EntryDto>()

            val durationMs = start.elapsedNow().inWholeMilliseconds

            networkLogger.info("Supabase.getEntryById success id=$id durationMs=$durationMs")

            if (ApiConfig.isDebug()) {
                val simple = itemToSimpleString(item)
                networkLogger.debug("Supabase.getEntryById item=$simple")
            }

            NetworkResult.Success(item)
        }.getOrElse { error ->
            networkLogger.error("Supabase.getEntryById failed id=$id", error)

            NetworkResult.Failure(error.message ?: "Supabase error")
        }

    override suspend fun upsertEntry(entry: EntryDto): NetworkResult<Unit> =
        runCatching {
            val start = TimeSource.Monotonic.markNow()

            networkLogger.info("Supabase.upsertEntry start table=$tableName id=${entry.id}")

            if (ApiConfig.isDebug()) {
                val simple = itemToSimpleString(entry)
                networkLogger.debug("Supabase.upsertEntry payload=$simple")
            }
            supabase.from(tableName)
                .upsert(listOf(entry))

            val durationMs = start.elapsedNow().inWholeMilliseconds

            networkLogger.info("Supabase.upsertEntry success id=${entry.id} durationMs=$durationMs")

            NetworkResult.Success(Unit)
        }.getOrElse { error ->
            networkLogger.error("Supabase.upsertEntry failed id=${entry.id}", error)
            NetworkResult.Failure(error.message ?: "Supabase error")
        }

    override suspend fun deleteEntryById(id: String): NetworkResult<Unit> =
        runCatching {
            val start = TimeSource.Monotonic.markNow()

            networkLogger.info("Supabase.deleteEntryById start table=$tableName id=$id")

            supabase.from(tableName)
                .delete {
                    filter { eq("id", id) }
                }

            val durationMs = start.elapsedNow().inWholeMilliseconds

            networkLogger.info("Supabase.deleteEntryById success id=$id durationMs=$durationMs")

            NetworkResult.Success(Unit)
        }.getOrElse { error ->
            networkLogger.error("Supabase.deleteEntryById failed id=$id", error)

            NetworkResult.Failure(error.message ?: "Supabase error")
        }
}
