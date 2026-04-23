package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.datasource.common.CommonEntriesDataSource
import com.kindaboii.journal.features.entries.impl.data.CommonEntriesDataSourceImpl
import com.kindaboii.journal.features.entries.impl.data.encryption.BrowserEntryCodePhraseStorage
import com.kindaboii.journal.features.entries.impl.data.encryption.EntryCodePhraseStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val jsEntriesModule = module {
    singleOf(::BrowserEntryCodePhraseStorage) { bind<EntryCodePhraseStorage>() }
    singleOf(::CommonEntriesDataSourceImpl) { bind<CommonEntriesDataSource>() }
}
