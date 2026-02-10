package com.kindaboii.journal.features.entries.impl.data.datasource

import com.kindaboii.journal.data.database.dao.entries.EntriesDao
import com.kindaboii.journal.features.entries.impl.data.mapper.toModel
import com.kindaboii.journal.features.entries.impl.data.mapper.toEntity
import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * NonJS implementation using SQLDelight + PowerSync.
 * Provides offline-first storage with automatic bidirectional sync to Supabase.
 */
class EntriesDataSourceImpl(
    private val entriesDao: EntriesDao
) : EntriesDataSource {
    override fun getEntries(): Flow<List<Entry>> =
        entriesDao
            .getEntries()
            .map { items ->
                items
                    .map {
                        it.toModel()
                    }
            }

    override suspend fun getEntryById(id: String): Entry? =
        entriesDao
            .getEntryById(id)
            ?.toModel()

    override suspend fun insertEntry(entry: Entry) {
        entriesDao
            .insertEntry(
                entry.toEntity()
            )
    }

    override suspend fun updateEntry(entry: Entry) {
        entriesDao
            .updateEntry(
                entry.toEntity()
            )
    }
}
