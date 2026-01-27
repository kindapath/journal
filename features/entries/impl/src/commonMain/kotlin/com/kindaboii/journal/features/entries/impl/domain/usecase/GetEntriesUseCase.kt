package com.kindaboii.journal.features.entries.impl.domain.usecase

import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.domain.model.Entry

class GetEntriesUseCase(
    private val repository: EntryRepository,
) {
    operator fun invoke(): List<Entry> = repository.getEntries()
}


