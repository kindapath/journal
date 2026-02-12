package com.kindaboii.journal.data.database.dao.entries

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.kindaboii.journal.data.database.SharedDatabase
import com.kindaboii.journal.features.entries.schema.Entries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class EntriesDaoImpl(
    private val db: SharedDatabase
) : EntriesDao {

    private fun databaseFlow() = flow { emit(db { it }) }

    override fun getEntries(): Flow<List<Entries>> =
        databaseFlow()
            .flatMapLatest { database ->
                database.entryDatabaseQueries
                    .getEntries()
                    .asFlow()
                    .mapToList(Dispatchers.Default)
            }

    override suspend fun getEntryById(id: String): Entries? =
        db { database ->
            database.entryDatabaseQueries
                .getEntryById(id)
                .asFlow()
                .mapToOneOrNull(Dispatchers.Default)
                .first()
        }

    override suspend fun insertEntry(entity: Entries) {
        db { database ->
            database.entryDatabaseQueries.insertEntry(entity)
        }
    }

    override suspend fun updateEntry(entity: Entries) {
        db { database ->
            database.entryDatabaseQueries.updateEntry(
                title = entity.title,
                body = entity.body,
                user_id = entity.user_id,
                mood_value = entity.mood_value,
                mood_emotions = entity.mood_emotions,
                mood_influences = entity.mood_influences,
                updated_at = entity.updated_at,
                deleted_at = entity.deleted_at,
                id = entity.id,
            )
        }
    }
}
