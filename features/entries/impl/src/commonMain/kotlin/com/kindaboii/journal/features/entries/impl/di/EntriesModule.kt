package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.api.EntriesFeatureApi
import com.kindaboii.journal.features.entries.impl.EntriesFeatureApiImpl
import com.kindaboii.journal.features.entries.impl.data.encryption.EntryEncryptionService
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.presentation.create.CreateEntryViewModel
import com.kindaboii.journal.features.entries.impl.presentation.entries.EntriesViewModel
import com.kindaboii.journal.features.entries.impl.presentation.unlock.EntryEncryptionUnlockViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val entriesModule = module {
    singleOf(::EntriesFeatureApiImpl) { bind<EntriesFeatureApi>() }
    singleOf(::EntryEncryptionService)
    singleOf(::EntryRepository)

    viewModelOf(::EntriesViewModel)
    viewModelOf(::CreateEntryViewModel)
    viewModelOf(::EntryEncryptionUnlockViewModel)
}
