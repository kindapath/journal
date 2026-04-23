package com.kindaboii.journal.features.entries.impl.data

import com.kindaboii.journal.data.database.dao.entries.EntriesDao
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.datasource.common.CommonEntriesDataSource
import com.kindaboii.journal.features.entries.impl.data.encryption.EntryEncryptionService
import com.kindaboii.journal.features.entries.impl.data.mapper.toEntity
import com.kindaboii.journal.features.entries.impl.data.mapper.toModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * NonJS implementation using SQLDelight + PowerSync.
 * Provides offline-first storage with automatic bidirectional sync to Supabase.
 */
class CommonEntriesDataSourceImpl(
    private val entriesDao: EntriesDao,
    private val encryptionService: EntryEncryptionService,
) : CommonEntriesDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getEntries(userId: String): Flow<List<Entry>> =
        encryptionService.state.flatMapLatest { encryptionState ->
            if (!encryptionState.isUnlockedFor(userId)) {
                flowOf(emptyList())
            } else {
                entriesDao
                    .getEntries(userId)
                    .map { items ->
                        try {
                            val decryptedEntries = mutableListOf<Entry>()
                            for (item in items) {
                                decryptedEntries += encryptionService.decryptEntry(item.toModel())
                            }
                            decryptedEntries
                        } catch (e: CancellationException) {
                            throw e
                        } catch (_: Exception) {
                            encryptionService.lock()
                            emptyList()
                        }
                    }
            }
        }

    override suspend fun getEntriesForEncryptionValidation(userId: String): List<Entry> =
        entriesDao
            .getEntries(userId)
            .first()
            .map { it.toModel() }

    override fun observeEntriesForEncryptionValidation(userId: String): Flow<List<Entry>> =
        entriesDao
            .getEntries(userId)
            .map { items -> items.map { it.toModel() } }

    override suspend fun getEntryById(id: String, userId: String): Entry? =
        if (!encryptionService.state.value.isUnlockedFor(userId)) {
            null
        } else {
            try {
                entriesDao
                    .getEntryById(id, userId)
                    ?.toModel()
                    ?.let { encryptionService.decryptEntry(it) }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                encryptionService.lock()
                null
            }
        }

    override suspend fun insertEntry(entry: Entry) {
        entriesDao
            .insertEntry(
                encryptionService.encryptEntry(entry).toEntity()
            )
    }

    override suspend fun updateEntry(entry: Entry) {
        entriesDao
            .updateEntry(
                encryptionService.encryptEntry(entry).toEntity()
            )
    }
}
