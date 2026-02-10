package com.kindaboii.journal.data.database.sync

import com.kindaboii.journal.data.database.PowerSyncDatabaseProvider
import com.powersync.connector.supabase.SupabaseConnector

/**
 * PowerSync implementation of SyncManager for NonJS platforms.
 * Connects PowerSync database to Supabase backend using the provided connector.
 */
class PowerSyncManager(
    private val powerSyncDatabaseProvider: PowerSyncDatabaseProvider,
    private val supabaseConnector: SupabaseConnector,
) : SyncManager {
    override suspend fun startSync() {
        powerSyncDatabaseProvider
            .powerSyncDatabase
            .connect(connector = supabaseConnector)
    }

    override suspend fun stopSync() {
        powerSyncDatabaseProvider
            .powerSyncDatabase
            .disconnect()
    }
}
