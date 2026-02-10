package com.kindaboii.journal.di

import com.kindaboii.journal.data.database.DatabaseDriverFactory
import com.kindaboii.journal.features.entries.impl.di.jsEntriesModule
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
    includes(jsEntriesModule)
}
