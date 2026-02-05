package com.kindaboii.journal.di

import android.app.Application
import com.kindaboii.journal.data.database.DatabaseDriverFactory
import com.kindaboii.journal.data.database.PowerSyncDatabaseProvider
import com.kindaboii.journal.features.entries.impl.di.nonJsEntriesModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory(androidContext()) }
    single<PowerSyncDatabaseProvider> { get<DatabaseDriverFactory>() } // TODO: looks weird
    includes(powerSyncModule()) // TODO: looks weird
    includes(nonJsEntriesModule)
}

fun initKoin(application: Application) {
    initKoin {
        androidContext(application)
    }
}
