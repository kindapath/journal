package com.kindaboii.journal.di

import com.kindaboii.journal.data.database.DatabaseDriverFactory
import com.kindaboii.journal.data.database.PowerSyncDatabaseProvider
import com.kindaboii.journal.features.entries.impl.di.jvmEntriesStorageModule
import com.kindaboii.journal.features.entries.impl.di.nonJsEntriesModule
import com.kindaboii.journal.theme.JvmThemePreferenceStorage
import com.kindaboii.journal.theme.ThemePreferenceStorage
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
    single<PowerSyncDatabaseProvider> { get<DatabaseDriverFactory>() }
    single<ThemePreferenceStorage> { JvmThemePreferenceStorage() }
    includes(powerSyncModule())
    includes(nonJsEntriesModule)
    includes(jvmEntriesStorageModule)
}
