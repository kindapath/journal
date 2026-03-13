package com.kindaboii.journal.data.di

import com.kindaboii.journal.data.database.sync.NoOpSyncManager
import com.kindaboii.journal.data.database.sync.SyncManager
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * JS platform-specific database module.
 * Registers NoOpSyncManager since JS uses Realtime subscriptions per data source.
 */
actual fun platformDatabaseModule(): Module = module {
    singleOf(::NoOpSyncManager) { bind<SyncManager>() }
}
