package com.kindaboii.journal.features.entries.impl.di

import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.data.repository.FakeEntryRepository
import com.kindaboii.journal.features.entries.impl.domain.usecase.GetEntriesUseCase
import com.kindaboii.journal.features.entries.impl.presentation.create.CreateEntryViewModel
import com.kindaboii.journal.features.entries.impl.presentation.entries.EntriesViewModel
import org.koin.dsl.module

val entriesModule = module {
    single<EntryRepository> { FakeEntryRepository() }
    factory { GetEntriesUseCase(get()) }
    factory { EntriesViewModel(get()) }
    factory { CreateEntryViewModel() }
}


