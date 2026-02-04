package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.database.datasource.local.LocalDataSource
import com.kindaboii.journal.features.entries.impl.data.database.datasource.local.LocalDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * NonJS-specific module that provides LocalDataSource using SQLDelight
 * for Android, iOS, and JVM platforms.
 */
val nonJsEntriesModule = module {
    // Provide LocalDataSource with SQLDelight-based implementation
    singleOf(::LocalDataSourceImpl) { bind<LocalDataSource>() }
}
