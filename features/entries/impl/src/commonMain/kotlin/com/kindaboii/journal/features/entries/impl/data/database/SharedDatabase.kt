package com.kindaboii.journal.features.entries.impl.data.database

import app.cash.sqldelight.ColumnAdapter
import com.kindaboii.journal.EntryEntity
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString

class SharedDatabase(private val databaseDriverFactory: DatabaseDriverFactory) {
    private var database: EntryDatabase? = null


    private val listStringAdapter = object : ColumnAdapter<List<String>, String> {
        override fun decode(databaseValue: String): List<String> =
            decodeFromString(databaseValue)

        override fun encode(value: List<String>): String =
            encodeToString(value)
    }

    private suspend fun initDatabase() {
        database.takeIf { it != null } ?: run {
            database = EntryDatabase(
                databaseDriverFactory.createDriver(),
                entryEntityAdapter = EntryEntity.Adapter(
                    moodEmotionsAdapter = listStringAdapter,
                    moodInfluencesAdapter = listStringAdapter
                )
            )
        }
    }

    suspend operator fun <R> invoke(block: suspend (EntryDatabase) -> R): R {
        initDatabase()
        return database.takeIf { it != null }?.let {
            block(it)
        } ?: throw IllegalStateException("Database is not initialized")
    }
}

