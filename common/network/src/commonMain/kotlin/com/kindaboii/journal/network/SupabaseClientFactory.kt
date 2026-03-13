package com.kindaboii.journal.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

fun createSupabaseClient(): SupabaseClient =
    createSupabaseClient(
        supabaseUrl = ApiConfig.SUPABASE_URL,
        supabaseKey = ApiConfig.SUPABASE_CLIENT_API_KEY,
    ) {

        defaultLogLevel = LogLevel.INFO
        install(Auth)
        install(Postgrest) {
            defaultSchema = ApiConfig.SUPABASE_SCHEMA
        }
        install(Realtime)

    }
