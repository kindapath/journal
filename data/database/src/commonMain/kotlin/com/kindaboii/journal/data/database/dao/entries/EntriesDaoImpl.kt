package com.kindaboii.journal.data.database.dao.entries

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.kindaboii.journal.data.database.SharedDatabase
import com.kindaboii.journal.features.entries.schema.EntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class EntriesDaoImpl(private val db: SharedDatabase) : EntriesDao {
    override fun getEntries(): Flow<List<EntryEntity>> = flow {
        val entriesFlow = db { database ->
            database.entryDatabaseQueries
                .getEntries()
                .asFlow()
                .mapToList(Dispatchers.Default)
        }
        emitAll(entriesFlow)
    }

    override fun getAllEntries(): Flow<List<EntryEntity>> = flow {
        val entriesFlow = db { database ->
            database.entryDatabaseQueries
                .getAllEntries()
                .asFlow()
                .mapToList(Dispatchers.Default)
        }
        emitAll(entriesFlow)
    }

    override fun getEntryById(id: String): Flow<EntryEntity?> = flow {
        val entryFlow = db { database ->
            database.entryDatabaseQueries
                .getEntryById(id)
                .asFlow()
                .mapToOneOrNull(Dispatchers.Default)
        }
        emitAll(entryFlow)
    }

    override suspend fun insertEntry(entity: EntryEntity) {
        db { database ->
            database.entryDatabaseQueries.insertEntry(entity)
        }
    }

    override suspend fun updateEntry(entity: EntryEntity) {
        db { database ->
            database.entryDatabaseQueries.updateEntry(
                title = entity.title,
                body = entity.body,
                moodValue = entity.moodValue,
                moodEmotions = entity.moodEmotions,
                moodInfluences = entity.moodInfluences,
                updatedAt = entity.updatedAt,
                deletedAt = entity.deletedAt,
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

    override suspend fun replaceAll(entries: List<EntryEntity>) {
        db { database ->
            val q = database.entryDatabaseQueries
            q.transaction {
                q.deleteAllEntries()
                entries.forEach { entry ->
                    q.insertEntry(entry)
                }
            }
        }
    }

}
