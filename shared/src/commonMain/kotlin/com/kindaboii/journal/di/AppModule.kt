package com.kindaboii.journal.di

import com.kindaboii.journal.data.repository.EntryRepository
import com.kindaboii.journal.data.repository.FakeEntryRepository
import com.kindaboii.journal.domain.usecase.GetEntriesUseCase
import com.kindaboii.journal.presentation.entries.EntriesViewModel
import org.koin.dsl.module

val appModule = module {
    single<EntryRepository> { FakeEntryRepository() }
    factory { GetEntriesUseCase(get()) }
    factory { EntriesViewModel(get()) }
}
