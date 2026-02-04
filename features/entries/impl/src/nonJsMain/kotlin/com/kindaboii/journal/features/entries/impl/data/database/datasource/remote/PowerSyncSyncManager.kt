package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote

import com.kindaboii.journal.data.database.PowerSyncDatabaseProvider
import com.kindaboii.journal.network.ApiConfig
import com.powersync.connector.supabase.SupabaseConnector

class PowerSyncSyncManager(
    private val powerSyncDatabaseProvider: PowerSyncDatabaseProvider,
    private val supabaseConnector: SupabaseConnector,
) : SyncManager {
    override suspend fun startSync() {
        if (ApiConfig.POWERSYNC_URL.isBlank()) return

        if (ApiConfig.POWERSYNC_DEV_TOKEN.isBlank()) {
            if (!ApiConfig.SUPABASE_ANON_AUTH_ENABLED) return
            val loginResult = runCatching { supabaseConnector.loginAnonymously() }
            if (loginResult.isFailure) return
        }
        powerSyncDatabaseProvider.powerSyncDatabase.connect(connector = supabaseConnector)
    }

    override suspend fun stopSync() {
        powerSyncDatabaseProvider.powerSyncDatabase.disconnect()
    }
}
