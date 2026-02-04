package com.kindaboii.journal.di

import com.kindaboii.journal.data.database.DatabaseDriverFactory
import com.kindaboii.journal.data.database.PowerSyncDatabaseProvider
import com.kindaboii.journal.features.entries.impl.di.nonJsEntriesModule
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
    single<PowerSyncDatabaseProvider> { get<DatabaseDriverFactory>() }
    includes(powerSyncModule())
    includes(nonJsEntriesModule)
}
