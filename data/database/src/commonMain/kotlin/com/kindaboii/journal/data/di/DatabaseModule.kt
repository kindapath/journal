package com.kindaboii.journal.data.di

import com.kindaboii.journal.data.database.SharedDatabase
import com.kindaboii.journal.data.database.dao.entries.EntriesDao
import com.kindaboii.journal.data.database.dao.entries.EntriesDaoImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    singleOf(::SharedDatabase)
    singleOf(::EntriesDaoImpl) { bind<EntriesDao>() }
}
