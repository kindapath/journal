package com.kindaboii.journal.features.entries.impl.data.datasource

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models.EntryDto
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.models.toModel
import com.kindaboii.journal.features.entries.impl.data.mapper.toDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * JS implementation using Supabase client directly.
 * No offline storage - provides online-only access with Realtime subscriptions
 * for automatic updates when data changes on the server.
 */
class EntriesDataSourceImpl(
    private val supabase: SupabaseClient
) : EntriesDataSource {

    // Shared scope for Realtime subscriptions
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Shared Flow for entries to avoid multiple subscriptions
    private val entriesFlow = callbackFlow {
        // Fetch initial data
        suspend fun fetchAndEmit() {
            try {
                val entries = supabase.from("entries")
                    .select()
                    .decodeList<EntryDto>()
                    .map { it.toModel() }
                    .filter { it.deletedAt == null }
                    .sortedByDescending { it.createdAt }

                send(entries)
            } catch (e: CancellationException) {
                // Re-throw cancellation exceptions - they're used for flow control
                throw e
            } catch (e: Exception) {
                console.error("Error fetching entries:", e)
                send(emptyList())
            }
        }

        // Initial fetch
        fetchAndEmit()

        console.log("Setting up Realtime subscription for entries...")

        // Subscribe to Realtime changes
        val channel = supabase.channel("entries-channel")

        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "entries"
        }

        // Subscribe to start receiving events
        console.log("Subscribing to channel...")
        channel.subscribe()
        console.log("Channel subscribed!")

        // Listen for changes
        val job = launch {
            console.log("Started collecting from Realtime channel...")
            try {
                changeFlow.collect { action ->
                    console.log("✅ Realtime event received:", action.toString())
                    // Refetch all entries on any change
                    fetchAndEmit()
                }
            } catch (e: Exception) {
                console.error("Error collecting from changeFlow:", e)
            }
        }

        // Clean up on cancellation
        awaitClose {
            job.cancel()
            // Launch coroutine for async cleanup
            CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
                try {
                    channel.unsubscribe()
                } catch (e: Exception) {
                    console.error("Error unsubscribing from channel:", e)
                }
            }
        }
    }.catch { e ->
        console.error("Error in entries flow:", e)
        emit(emptyList())
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000), // Keep alive for 5s after last subscriber
        replay = 1 // Replay last value to new subscribers
    )

    override fun getEntries(): Flow<List<Entry>> = entriesFlow

    override suspend fun getEntryById(id: String): Entry? {
        try {
            return supabase.from("entries")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingleOrNull<EntryDto>()
                ?.toModel()
        } catch (e: CancellationException) {
            // Re-throw cancellation exceptions - they're used for flow control
            throw e
        } catch (e: Exception) {
            console.error("Error fetching entry by id:", e)
            return null
        }
    }

    override suspend fun insertEntry(entry: Entry) {
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
}
