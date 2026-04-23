package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.encryption.EntryCodePhraseStorage
import com.kindaboii.journal.features.entries.impl.data.encryption.NoOpEntryCodePhraseStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val iosEntriesStorageModule = module {
    singleOf(::NoOpEntryCodePhraseStorage) { bind<EntryCodePhraseStorage>() }
}
