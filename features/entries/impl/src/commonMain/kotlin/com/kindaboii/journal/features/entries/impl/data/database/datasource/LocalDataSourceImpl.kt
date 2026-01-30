package com.kindaboii.journal.features.entries.impl.data.database.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.kindaboii.journal.features.entries.impl.data.database.SharedDatabase
import com.kindaboii.journal.features.entries.impl.data.mapper.toDomain
import com.kindaboii.journal.features.entries.impl.data.mapper.toEntity
import com.kindaboii.journal.features.entries.impl.domain.models.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class LocalDataSourceImpl(private val db: SharedDatabase) : LocalDataSource {
    override fun getEntries(): Flow<List<Entry>> = flow {
        val entriesFlow = db { database ->
            database.entryDatabaseQueries
                .getEntries()
                .asFlow()
                .mapToList(Dispatchers.Default)
        }
        emitAll(entriesFlow.map { items -> items.map { it.toDomain() } })
    }

    override fun getEntryById(id: String): Flow<Entry?> = flow {
        val entryFlow = db { database ->
            database.entryDatabaseQueries
                .getEntryById(id)
                .asFlow()
                .mapToOneOrNull(Dispatchers.Default)
        }
        emitAll(entryFlow.map { it?.toDomain() })
    }

    override suspend fun insertEntry(entry: Entry) {
        db { database ->
            database.entryDatabaseQueries.insertEntry(entry.toEntity())
        }
    }

    override suspend fun updateEntry(entry: Entry) {
        db { database ->
            val entity = entry.toEntity()
            database.entryDatabaseQueries.updateEntry(
                title = entity.title,
                body = entity.body,
                moodValue = entity.moodValue,
                moodEmotions = entity.moodEmotions,
                moodInfluences = entity.moodInfluences,
                updatedAt = entity.updatedAt,
                id = entity.id,
            )
        }
    }

    override suspend fun deleteEntryById(id: String) {
        db { database ->
            database.entryDatabaseQueries.deleteEntryById(id)
        }
    }

    override suspend fun deleteAllEntries() {
        db { database ->
            database.entryDatabaseQueries.deleteAllEntries()
        }
    }

    override suspend fun replaceAll(entries: List<Entry>) {
        db { database ->
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
}
