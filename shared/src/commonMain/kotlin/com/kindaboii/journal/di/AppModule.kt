package com.kindaboii.journal.di

import com.kindaboii.journal.data.di.databaseModule
import com.kindaboii.journal.features.auth.impl.di.authModule
import com.kindaboii.journal.features.entries.impl.di.entriesModule
import com.kindaboii.journal.lifecycle.AppLifecycleManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    includes(
        databaseModule,
        authModule,
        entriesModule,
        networkModule
    )
    singleOf(::AppLifecycleManager)
}
