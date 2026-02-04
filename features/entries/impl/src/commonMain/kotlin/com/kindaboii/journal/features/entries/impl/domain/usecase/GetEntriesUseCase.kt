package com.kindaboii.journal.features.entries.impl.domain.usecase

import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.api.models.Entry
import kotlinx.coroutines.flow.Flow

class GetEntriesUseCase(
    private val repository: EntryRepository,
) {
    operator fun invoke(): Flow<List<Entry>> = repository.getEntries()
}


