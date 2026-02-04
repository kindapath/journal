package com.kindaboii.journal.di

import com.kindaboii.journal.network.ConsoleNetworkLogger
import com.kindaboii.journal.network.NetworkLogger
import com.kindaboii.journal.network.createHttpClient
import com.kindaboii.journal.network.createSupabaseClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single { createSupabaseClient() }
    singleOf(::ConsoleNetworkLogger) { bind<NetworkLogger>() }
}
