package com.kindaboii.journal.data.database

import app.cash.sqldelight.ColumnAdapter
import com.kindaboii.journal.data.database.DatabaseConstants.DATABASE_NOT_INITIALIZED
import com.kindaboii.journal.features.entries.schema.EntryEntity
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.json.Json.Default.encodeToString

class SharedDatabase(private val databaseDriverFactory: DatabaseDriverFactory) {
    private var database: AppDatabase? = null

    private val listStringAdapter = object : ColumnAdapter<List<String>, String> {
        override fun decode(databaseValue: String): List<String> =
            decodeFromString(databaseValue)

        override fun encode(value: List<String>): String =
            encodeToString(value)
    }

    private suspend fun initDatabase() {
        database.takeIf { it != null } ?: run {
            database = AppDatabase(
                databaseDriverFactory.createDriver(),
                entryEntityAdapter = EntryEntity.Adapter(
                    moodEmotionsAdapter = listStringAdapter,
                    moodInfluencesAdapter = listStringAdapter
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
