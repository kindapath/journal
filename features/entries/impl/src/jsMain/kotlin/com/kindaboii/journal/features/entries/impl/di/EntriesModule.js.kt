package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.database.datasource.local.LocalDataSource
import com.kindaboii.journal.features.entries.impl.data.database.datasource.local.LocalDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * JS-specific module that overrides LocalDataSource to use Supabase directly
 * instead of SQLDelight.
 */
val jsEntriesModule = module {
    // Override the common LocalDataSource with JS-specific implementation
    singleOf(::LocalDataSourceImpl) { bind<LocalDataSource>() }
}
