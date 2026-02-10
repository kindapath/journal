package com.kindaboii.journal.data.database.sync

/**
 * No-op sync manager for JS platform.
 *
 * JS/Web uses direct Supabase client with Realtime subscriptions in data sources,
 * so no separate sync infrastructure is needed. Each data source manages its own
 * Realtime subscriptions for automatic updates.
 */
class NoOpSyncManager : SyncManager {
    override suspend fun startSync() = Unit
    override suspend fun stopSync() = Unit
}
