package com.kindaboii.journal.features.entries.impl.data.datasource.common

import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.Flow

/**
 * Platform-agnostic synchronized storage for entries.
 *
 * This interface abstracts platform-specific storage implementations:
 * - **NonJS** (Android/iOS/Desktop): Uses SQLDelight for local storage with PowerSync
 *   providing automatic bidirectional sync to Supabase (offline-first architecture)
 * - **JS** (Web): Uses Supabase client directly with Realtime subscriptions for
 *   automatic updates (online-only architecture)
 *
 * From the repository's perspective, both implementations provide the same semantics:
 * reactive data access with automatic synchronization.
 */
interface CommonEntriesDataSource {
    /**
     * Returns a reactive stream of all non-deleted entries.
     * Automatically updates when entries change.
     */
    fun getEntries(userId: String): Flow<List<Entry>>

    /**
     * Returns entries before field decryption.
     *
     * This is intentionally narrow and used only to validate whether the entered
     * encryption passphrase can decrypt already-synced encrypted entries.
     */
    suspend fun getEntriesForEncryptionValidation(userId: String): List<Entry>

    /**
     * Observes entries before field decryption.
     *
     * Used by the unlock screen to switch from "create" to "unlock" if encrypted
     * entries arrive after the first local sync on a new device.
     */
    fun observeEntriesForEncryptionValidation(userId: String): Flow<List<Entry>>

    /**
     * Returns a single entry lookup by ID.
     * Returns null if entry doesn't exist or is deleted.
     */
    suspend fun getEntryById(id: String, userId: String): Entry?

    /**
     * Inserts a new entry into storage.
     * Changes will be automatically synced to remote backend.
     */
    suspend fun insertEntry(entry: Entry)

    /**
     * Updates an existing entry in storage.
     * Changes will be automatically synced to remote backend.
     */
    suspend fun updateEntry(entry: Entry)
}
