package com.kindaboii.journal.features.entries.impl.data.database.datasource

import com.kindaboii.journal.features.entries.impl.domain.models.Entry

interface LocalDataSource {
    suspend fun getEntries(): List<Entry>
    suspend fun replaceAll(entries: List<Entry>)
}