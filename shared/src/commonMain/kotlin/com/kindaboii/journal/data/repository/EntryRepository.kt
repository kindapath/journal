package com.kindaboii.journal.data.repository

import com.kindaboii.journal.domain.model.Entry

interface EntryRepository {
    fun getEntries(): List<Entry>
}
