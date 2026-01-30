package com.kindaboii.journal.di

import android.app.Application
import com.kindaboii.journal.features.entries.impl.data.database.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory(androidContext()) }
}

fun initKoin(application: Application) {
    initKoin {
        androidContext(application)
    }
}
