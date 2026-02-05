package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.database.datasource.local.LocalDataSource
import com.kindaboii.journal.features.entries.impl.data.database.datasource.local.LocalDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val nonJsEntriesModule = module {
    singleOf(::LocalDataSourceImpl) { bind<LocalDataSource>() }
}
