package com.kindaboii.journal.features.entries.api

import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.Flow

interface EntriesFeatureApi {
    fun getEntries(): Flow<List<Entry>>
}
