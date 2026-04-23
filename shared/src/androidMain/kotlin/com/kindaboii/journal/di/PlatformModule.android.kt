package com.kindaboii.journal.di

import android.app.Application
import com.kindaboii.journal.data.database.DatabaseDriverFactory
import com.kindaboii.journal.data.database.PowerSyncDatabaseProvider
import com.kindaboii.journal.features.entries.impl.di.androidEntriesStorageModule
import com.kindaboii.journal.features.entries.impl.di.nonJsEntriesModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory(androidContext()) }
    single<PowerSyncDatabaseProvider> { get<DatabaseDriverFactory>() }
    includes(powerSyncModule())
    includes(nonJsEntriesModule)
    includes(androidEntriesStorageModule)
}

fun initKoin(application: Application) {
    initKoin {
        androidContext(application)
    }
}
