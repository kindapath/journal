package com.kindaboii.journal.data.database.sync

/**
 * Manages database synchronization between local storage and remote backend.
 *
 * Platform implementations:
 * - NonJS (Android/iOS/Desktop): Connects PowerSync for automatic bidirectional sync
 * - JS (Web): No-op implementation (Realtime subscriptions handled per data source)
 */
interface SyncManager {
    /**
     * Starts the synchronization process.
     * Should be called once during app initialization.
     */
    suspend fun startSync()

    /**
     * Stops the synchronization process.
     * Should be called during app shutdown.
     */
    suspend fun stopSync()
}
