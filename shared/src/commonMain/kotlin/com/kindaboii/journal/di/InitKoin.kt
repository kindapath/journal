package com.kindaboii.journal.di

import com.kindaboii.journal.lifecycle.AppLifecycleManager
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

private var koinRef: Koin? = null

fun initKoin(appDeclaration: KoinApplication.() -> Unit = {}) {
    if (koinRef != null) return
    val application = startKoin {
        appDeclaration()
        modules(appModule, platformModule())
    }
    koinRef = application.koin

    // Start app-level services (sync, etc.)
    application.koin.get<AppLifecycleManager>().onAppStart()
}

expect fun platformModule(): Module
