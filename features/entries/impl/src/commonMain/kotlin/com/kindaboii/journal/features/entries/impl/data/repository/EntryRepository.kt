package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.entries.impl.domain.models.Entry

interface EntryRepository {
    fun getEntries(): List<Entry>
}


