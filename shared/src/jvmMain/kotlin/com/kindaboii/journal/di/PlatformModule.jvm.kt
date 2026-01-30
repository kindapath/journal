package com.kindaboii.journal.di

import com.kindaboii.journal.features.entries.impl.data.database.DatabaseDriverFactory
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
}
