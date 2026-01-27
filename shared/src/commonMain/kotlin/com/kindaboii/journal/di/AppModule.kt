package com.kindaboii.journal.di

import com.kindaboii.journal.features.entries.impl.di.entriesModule
import org.koin.dsl.module

val appModule = module {
    includes(entriesModule)
}
