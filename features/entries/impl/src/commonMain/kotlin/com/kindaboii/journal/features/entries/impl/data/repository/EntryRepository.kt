package com.kindaboii.journal.features.entries.impl.data.repository

import com.kindaboii.journal.features.entries.impl.domain.model.Entry

interface EntryRepository {
    fun getEntries(): List<Entry>
}


