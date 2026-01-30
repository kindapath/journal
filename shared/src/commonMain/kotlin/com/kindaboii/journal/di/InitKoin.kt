package com.kindaboii.journal.di

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
}

fun getKoin(): Koin = koinRef ?: error("Koin is not initialized. Call initKoin() first.")

expect fun platformModule(): Module
