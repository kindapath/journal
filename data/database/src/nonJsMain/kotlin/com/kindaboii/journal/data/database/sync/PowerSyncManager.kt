package com.kindaboii.journal.data.database.sync

import com.kindaboii.journal.data.database.PowerSyncDatabaseProvider
import com.powersync.connector.supabase.SupabaseConnector
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * PowerSync implementation of SyncManager for NonJS platforms.
 * Connects PowerSync database to Supabase backend.
 */
class PowerSyncManager(
    private val powerSyncDatabaseProvider: PowerSyncDatabaseProvider,
) : SyncManager, KoinComponent {
    override suspend fun startSync() {
        powerSyncDatabaseProvider
            .powerSyncDatabase
            .connect(connector = get<SupabaseConnector>())
    }

    override suspend fun stopSync() {
        powerSyncDatabaseProvider
            .powerSyncDatabase
            .disconnect()
    }
}
