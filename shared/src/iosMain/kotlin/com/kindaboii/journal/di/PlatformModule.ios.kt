package com.kindaboii.journal.di

import com.kindaboii.journal.data.database.DatabaseDriverFactory
import com.kindaboii.journal.data.database.PowerSyncDatabaseProvider
import com.kindaboii.journal.features.entries.impl.di.iosEntriesStorageModule
import com.kindaboii.journal.features.entries.impl.di.nonJsEntriesModule
import com.kindaboii.journal.theme.IosThemePreferenceStorage
import com.kindaboii.journal.theme.ThemePreferenceStorage
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
    single<PowerSyncDatabaseProvider> { get<DatabaseDriverFactory>() }
    single<ThemePreferenceStorage> { IosThemePreferenceStorage() }
    includes(powerSyncModule())
    includes(nonJsEntriesModule)
    includes(iosEntriesStorageModule)
}
