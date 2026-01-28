package com.kindaboii.journal.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(listOf(appModule))
    }
}
