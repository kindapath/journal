package com.kindaboii.journal.data.di

import com.kindaboii.journal.data.database.sync.PowerSyncManager
import com.kindaboii.journal.data.database.sync.SyncManager
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * NonJS platform-specific database module.
 * Registers PowerSyncManager for automatic bidirectional sync.
 *
 * Dependencies expected from parent scope:
 * - PowerSyncDatabaseProvider
 * - SupabaseConnector (provided by shared module's powerSyncModule)
 */
actual fun platformDatabaseModule(): Module = module {
    singleOf(::PowerSyncManager) { bind<SyncManager>() }
}
