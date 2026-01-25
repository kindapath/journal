package com.kindaboii.journal.domain.usecase

import com.kindaboii.journal.data.repository.EntryRepository
import com.kindaboii.journal.domain.model.Entry

class GetEntriesUseCase(
    private val repository: EntryRepository,
) {
    operator fun invoke(): List<Entry> = repository.getEntries()
}
