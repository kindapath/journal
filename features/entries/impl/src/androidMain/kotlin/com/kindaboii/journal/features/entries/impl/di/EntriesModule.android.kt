package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.encryption.AndroidEntryCodePhraseStorage
import com.kindaboii.journal.features.entries.impl.data.encryption.EntryCodePhraseStorage
import org.koin.dsl.module

val androidEntriesStorageModule = module {
    single<EntryCodePhraseStorage> { AndroidEntryCodePhraseStorage(get()) }
}
