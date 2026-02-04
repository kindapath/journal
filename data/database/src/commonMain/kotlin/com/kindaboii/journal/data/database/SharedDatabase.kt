package com.kindaboii.journal.data.database

import app.cash.sqldelight.ColumnAdapter
import com.kindaboii.journal.data.database.DatabaseConstants.DATABASE_NOT_INITIALIZED
import com.kindaboii.journal.features.entries.schema.Entries
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString

class SharedDatabase(private val databaseDriverFactory: DatabaseDriverFactory) {
    private var database: AppDatabase? = null

    private val listStringAdapter = object : ColumnAdapter<List<String>, String> {
        override fun decode(databaseValue: String): List<String> {
            // Handle JSONB format from Supabase which comes back as quoted string
            val unescaped = if (databaseValue.startsWith("\"") && databaseValue.endsWith("\"")) {
                databaseValue.substring(1, databaseValue.length - 1).replace("\\\"", "\"")
            } else {
                databaseValue
            }
            return decodeFromString(unescaped)
        }

        override fun encode(value: List<String>): String =
            encodeToString(value)
    }

    private suspend fun initDatabase() {
        database.takeIf { it != null } ?: run {
            database = AppDatabase(
                databaseDriverFactory.createDriver(),
                entriesAdapter = Entries.Adapter(
                    mood_emotionsAdapter = listStringAdapter,
                    mood_influencesAdapter = listStringAdapter
                )
            )
        }
    }

    suspend operator fun <R> invoke(block: suspend (AppDatabase) -> R): R {
        initDatabase()
        return database.takeIf { it != null }?.let {
            block(it)
        } ?: throw IllegalStateException(DATABASE_NOT_INITIALIZED)
    }
}
