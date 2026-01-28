package com.kindaboii.journal.features.entries.impl.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.data.repository.FakeEntryRepository
import com.kindaboii.journal.features.entries.impl.domain.usecase.GetEntriesUseCase
import com.kindaboii.journal.features.entries.impl.presentation.create.CreateEntryViewModel
import com.kindaboii.journal.features.entries.impl.presentation.entries.EntriesViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf

import org.koin.dsl.module

val entriesModule = module {
    singleOf(::FakeEntryRepository) { bind<EntryRepository>() }
    factoryOf(::GetEntriesUseCase)
    viewModelOf(::EntriesViewModel)
    viewModelOf(::CreateEntryViewModel)
}
