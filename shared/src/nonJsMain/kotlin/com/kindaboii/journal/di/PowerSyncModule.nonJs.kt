package com.kindaboii.journal.di

import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.NonJsPowerSyncManager
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.SyncManager
import com.kindaboii.journal.network.ApiConfig
import com.powersync.connector.supabase.SupabaseConnector
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal fun powerSyncModule() = module {
    single {
        if (ApiConfig.POWERSYNC_DEV_TOKEN.isNotBlank()) {
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
    }
    singleOf(::NonJsPowerSyncManager) { bind<SyncManager>() }
}
