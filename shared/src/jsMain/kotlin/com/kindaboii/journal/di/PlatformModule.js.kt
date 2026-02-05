package com.kindaboii.journal.di

import com.kindaboii.journal.data.database.DatabaseDriverFactory
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.EmptyJsSyncManager
import com.kindaboii.journal.features.entries.impl.data.database.datasource.remote.SyncManager
import com.kindaboii.journal.features.entries.impl.di.jsEntriesModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
    singleOf(::EmptyJsSyncManager) { bind<SyncManager>() }
    includes(jsEntriesModule)
}
