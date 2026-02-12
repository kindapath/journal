package com.kindaboii.journal.di

import com.kindaboii.journal.network.ApiConfig
import com.kindaboii.journal.features.auth.api.AuthRepository
import com.powersync.connector.supabase.SupabaseConnector
import org.koin.dsl.module

internal fun powerSyncModule() = module {
    single<SupabaseConnector> {
        // Skip if PowerSync URL not configured
        if (ApiConfig.POWERSYNC_URL.isBlank()) {
            // Return a stub connector that won't be used
            return@single SupabaseConnector(
                supabaseUrl = "",
                supabaseKey = "",
                powerSyncEndpoint = "",
            )
        }

        val connector = if (ApiConfig.POWERSYNC_DEV_TOKEN.isNotBlank() && ApiConfig.DEBUG) {
            DevTokenSupabaseConnector(
                supabaseUrl = ApiConfig.SUPABASE_URL,
                supabaseKey = ApiConfig.SUPABASE_CLIENT_API_KEY,
                powerSyncEndpoint = ApiConfig.POWERSYNC_URL,
                devToken = ApiConfig.POWERSYNC_DEV_TOKEN,
            )
        } else {
            if (ApiConfig.POWERSYNC_DEV_TOKEN.isNotBlank() && !ApiConfig.DEBUG) {
                println("Ignoring POWERSYNC_DEV_TOKEN because debug mode is disabled.")
            }

            SessionTokenSupabaseConnector(
                supabaseUrl = ApiConfig.SUPABASE_URL,
                supabaseKey = ApiConfig.SUPABASE_CLIENT_API_KEY,
                powerSyncEndpoint = ApiConfig.POWERSYNC_URL,
                authRepository = get<AuthRepository>(),
            )
        }

        connector
    }
}
