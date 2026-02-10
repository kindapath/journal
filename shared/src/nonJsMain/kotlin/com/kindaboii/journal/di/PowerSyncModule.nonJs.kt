package com.kindaboii.journal.di

import com.kindaboii.journal.network.ApiConfig
import com.powersync.connector.supabase.SupabaseConnector
import kotlinx.coroutines.runBlocking
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

        val connector = if (ApiConfig.POWERSYNC_DEV_TOKEN.isNotBlank()) {
            DevTokenSupabaseConnector(
                supabaseUrl = ApiConfig.SUPABASE_URL,
                supabaseKey = ApiConfig.SUPABASE_CLIENT_API_KEY,
                powerSyncEndpoint = ApiConfig.POWERSYNC_URL,
                devToken = ApiConfig.POWERSYNC_DEV_TOKEN,
            )
        } else {
            SupabaseConnector(
                supabaseUrl = ApiConfig.SUPABASE_URL,
                supabaseKey = ApiConfig.SUPABASE_CLIENT_API_KEY,
                powerSyncEndpoint = ApiConfig.POWERSYNC_URL,
            )
        }

        // Handle anonymous login if needed
        if (ApiConfig.POWERSYNC_DEV_TOKEN.isBlank() && ApiConfig.SUPABASE_ANON_AUTH_ENABLED) {
            runBlocking {
                runCatching {
                    connector.loginAnonymously()
                }
            }
        }

        connector
    }
}
