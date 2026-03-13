package com.kindaboii.journal.features.entries.impl.data

import com.kindaboii.journal.domain.AuthService
import com.kindaboii.journal.domain.AuthState
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.datasource.common.CommonEntriesDataSource
import com.kindaboii.journal.features.entries.impl.data.datasource.remote.models.EntryDto
import com.kindaboii.journal.features.entries.impl.data.datasource.remote.models.toModel
import com.kindaboii.journal.features.entries.impl.data.mapper.toDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * JS implementation using Supabase client directly.
 * Realtime subscriptions run only while user is authenticated.
 */
class CommonEntriesDataSourceImpl(
    private val supabase: SupabaseClient,
    private val authService: AuthService,
) : CommonEntriesDataSource {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val entriesFlow = authService.authState
        .flatMapLatest { authState ->
            if (authState is AuthState.Authenticated) {
                authenticatedEntriesFlow()
            } else {
                emptyFlow()
            }
        }
        .catch { e ->
            console.error("Error in entries flow:", e)
            emit(emptyList())
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1,
        )

    override fun getEntries(userId: String): Flow<List<Entry>> =
        entriesFlow.map { entries ->
            entries.filter { it.userId == userId }
        }

    override suspend fun getEntryById(id: String, userId: String): Entry? {
        if (!isAuthenticated()) return null
        return try {
            supabase.from("entries")
                .select {
                    filter {
                        eq("id", id)
                        eq("user_id", userId)
                    }
                }
                .decodeSingleOrNull<EntryDto>()
                ?.toModel()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            console.error("Error fetching entry by id:", e)
            null
        }
    }

    override suspend fun insertEntry(entry: Entry) {
        requireAuthenticated()
        try {
            supabase.from("entries")
                .insert(entry.toDto())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            console.error("Error inserting entry:", e)
            throw e
        }
    }

    override suspend fun updateEntry(entry: Entry) {
        requireAuthenticated()
        try {
            supabase.from("entries")
                .update(entry.toDto()) {
                    filter {
                        eq("id", entry.id)
                    }
                }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            console.error("Error updating entry:", e)
            throw e
        }
    }

    private fun authenticatedEntriesFlow(): Flow<List<Entry>> = callbackFlow {
        suspend fun fetchAndEmit() {
            try {
                val currentUserId = authService.currentUserId().orEmpty()
                if (currentUserId.isBlank()) {
                    send(emptyList())
                    return
                }

                val entries = supabase.from("entries")
                    .select {
                        filter {
                            eq("user_id", currentUserId)
                        }
                    }
                    .decodeList<EntryDto>()
                    .map { it.toModel() }
                    .filter { it.deletedAt == null }
                    .sortedByDescending { it.createdAt }

                send(entries)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                console.error("Error fetching entries:", e)
                send(emptyList())
            }
        }

        fetchAndEmit()

        val channel = supabase.channel("entries-channel")
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "entries"
        }

        channel.subscribe()

        val job = launch {
            try {
                changeFlow.collect {
                    fetchAndEmit()
                }
            } catch (e: Exception) {
                console.error("Error collecting from changeFlow:", e)
            }
        }

        awaitClose {
            job.cancel()
            CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
                try {
                    channel.unsubscribe()
                } catch (e: Exception) {
                    console.error("Error unsubscribing from channel:", e)
                }
            }
        }
    }

    private fun requireAuthenticated() {
        check(isAuthenticated()) { "Operation requires an authenticated Supabase session" }
    }

    private fun isAuthenticated(): Boolean =
        authService.authState.value is AuthState.Authenticated
}
