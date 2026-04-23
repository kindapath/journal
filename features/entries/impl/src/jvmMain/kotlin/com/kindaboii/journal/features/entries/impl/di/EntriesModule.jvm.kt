package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.encryption.EntryCodePhraseStorage
import com.kindaboii.journal.features.entries.impl.data.encryption.JvmEntryCodePhraseStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val jvmEntriesStorageModule = module {
    singleOf(::JvmEntryCodePhraseStorage) { bind<EntryCodePhraseStorage>() }
}
