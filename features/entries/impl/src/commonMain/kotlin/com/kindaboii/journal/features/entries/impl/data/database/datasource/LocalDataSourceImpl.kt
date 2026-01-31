package com.kindaboii.journal.features.entries.impl.data.database.datasource

import com.kindaboii.journal.data.database.dao.entries.EntriesDao
import com.kindaboii.journal.features.entries.impl.data.mapper.toDomain
import com.kindaboii.journal.features.entries.impl.data.mapper.toEntity
import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSourceImpl(private val entriesDao: EntriesDao) : LocalDataSource {
    override fun getEntries(): Flow<List<Entry>> =
        entriesDao.getEntries().map { items -> items.map { it.toDomain() } }

    override fun getEntryById(id: String): Flow<Entry?> =
        entriesDao.getEntryById(id).map { it?.toDomain() }

    override suspend fun insertEntry(entry: Entry) {
        entriesDao.insertEntry(entry.toEntity())
    }

    override suspend fun updateEntry(entry: Entry) {
        entriesDao.updateEntry(entry.toEntity())
    }

    override suspend fun deleteEntryById(id: String) {
        entriesDao.deleteEntryById(id)
    }

    override suspend fun deleteAllEntries() {
        entriesDao.deleteAllEntries()
    }

    override suspend fun replaceAll(entries: List<Entry>) {
        entriesDao.replaceAll(entries.map { it.toEntity() })
    }
}
