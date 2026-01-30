package com.kindaboii.journal.features.entries.impl.data.database.datasource

import app.cash.sqldelight.async.coroutines.awaitAsList
import com.kindaboii.journal.features.entries.impl.data.database.SharedDatabase
import com.kindaboii.journal.features.entries.impl.data.mapper.toDomain
import com.kindaboii.journal.features.entries.impl.data.mapper.toEntity
import com.kindaboii.journal.features.entries.impl.domain.models.Entry

class LocalDataSourceImpl(private val db: SharedDatabase) : LocalDataSource {
    override suspend fun getEntries(): List<Entry> = db { database ->
        database.entryDatabaseQueries
            .getEntries()
            .awaitAsList()
            .map {
                it.toDomain()
            }
    }

    override suspend fun replaceAll(entries: List<Entry>) = db { database ->
        val q = database.entryDatabaseQueries
        q.transaction {
            q.deleteAllEntries()
            entries.forEach { entry ->
                val entity = entry.toEntity()
                q.insertEntry(entity)
            }
        }
    }
}