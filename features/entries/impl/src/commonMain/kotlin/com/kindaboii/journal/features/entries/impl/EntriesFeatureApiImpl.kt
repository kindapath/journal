package com.kindaboii.journal.features.entries.impl

import com.kindaboii.journal.features.entries.api.EntriesFeatureApi
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import kotlinx.coroutines.flow.Flow

class EntriesFeatureApiImpl(
    private val repository: EntryRepository,
) : EntriesFeatureApi {
    override fun getEntries(): Flow<List<Entry>> = repository.getEntries()
}