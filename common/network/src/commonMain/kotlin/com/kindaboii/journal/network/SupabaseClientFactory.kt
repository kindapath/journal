package com.kindaboii.journal.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

fun createSupabaseClient(): SupabaseClient {
    silenceSupabaseLogs()
    return createSupabaseClient(
        supabaseUrl = ApiConfig.SUPABASE_URL,
        supabaseKey = ApiConfig.SUPABASE_CLIENT_API_KEY,
    ) {
        defaultLogLevel = LogLevel.NONE
        install(Auth)
        install(Postgrest) {
            defaultSchema = ApiConfig.SUPABASE_SCHEMA
        }
        install(Realtime) 
    }
}

@OptIn(SupabaseInternal::class)
private fun silenceSupabaseLogs() {
    runCatching { SupabaseClient.LOGGER.setLevel(LogLevel.NONE) }
    runCatching { Auth.logger.setLevel(LogLevel.NONE) }
    runCatching { Postgrest.logger.setLevel(LogLevel.NONE) }
    runCatching { Realtime.logger.setLevel(LogLevel.NONE) }
}
