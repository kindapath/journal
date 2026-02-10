package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.datasource.EntriesDataSource
import com.kindaboii.journal.features.entries.impl.data.datasource.EntriesDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val nonJsEntriesModule = module {
    singleOf(::EntriesDataSourceImpl) { bind<EntriesDataSource>() }
}
