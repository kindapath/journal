package com.kindaboii.journal.di

import com.kindaboii.journal.features.auth.api.AuthRepository
import com.powersync.connector.supabase.SupabaseConnector
import com.powersync.connectors.PowerSyncCredentials

class SessionTokenSupabaseConnector(
    supabaseUrl: String,
    supabaseKey: String,
    powerSyncEndpoint: String,
    private val authRepository: AuthRepository,
) : SupabaseConnector(
    supabaseUrl = supabaseUrl,
    supabaseKey = supabaseKey,
    powerSyncEndpoint = powerSyncEndpoint,
) {
    override suspend fun fetchCredentials(): PowerSyncCredentials {
        val token = authRepository.currentAccessToken()
            ?: error("PowerSync credential request without an authenticated Supabase session")

        return PowerSyncCredentials(
            endpoint = powerSyncEndpoint,
            token = token,
        )
    }
}
