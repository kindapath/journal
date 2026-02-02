package com.kindaboii.journal.di

import com.kindaboii.journal.network.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
}
