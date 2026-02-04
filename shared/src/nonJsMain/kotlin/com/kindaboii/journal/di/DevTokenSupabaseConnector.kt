package com.kindaboii.journal.di

import com.powersync.connector.supabase.SupabaseConnector
import com.powersync.connectors.PowerSyncCredentials

class DevTokenSupabaseConnector(
    supabaseUrl: String,
    supabaseKey: String,
    powerSyncEndpoint: String,
    private val devToken: String,
) : SupabaseConnector(
    supabaseUrl = supabaseUrl,
    supabaseKey = supabaseKey,
    powerSyncEndpoint = powerSyncEndpoint,
) {
    override suspend fun fetchCredentials(): PowerSyncCredentials =
        PowerSyncCredentials(
            endpoint = powerSyncEndpoint,
            token = devToken,
        )
}
