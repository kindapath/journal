package com.kindaboii.journal.features.entries.impl

import androidx.compose.runtime.Composable
import com.kindaboii.journal.features.entries.api.EntriesFeatureApi
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.impl.navigation.EntriesNavigation as EntriesNavigationImpl
import kotlinx.coroutines.flow.Flow

class EntriesFeatureApiImpl(
    private val repository: EntryRepository,
) : EntriesFeatureApi {
    override fun getEntries(): Flow<List<Entry>> = repository.getEntries()

    @Composable
    override fun EntriesNavigation(onSignOut: () -> Unit) {
        EntriesNavigationImpl(onSignOut = onSignOut)
    }
}
