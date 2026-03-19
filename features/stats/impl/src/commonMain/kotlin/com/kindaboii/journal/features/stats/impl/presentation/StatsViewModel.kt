package com.kindaboii.journal.features.stats.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.api.EntriesFeatureApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class StatsViewModel(
    private val entriesFeatureApi: EntriesFeatureApi,
) : ViewModel() {

    private val _viewState = MutableStateFlow<StatsViewState>(StatsViewState.Loading)
    val viewState: StateFlow<StatsViewState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            entriesFeatureApi.getEntries().collect { entries ->
                val tz = TimeZone.currentSystemDefault()
                val today = Clock.System.now().toLocalDateTime(tz).date
                _viewState.value = StatsCalculator.computeStats(entries, today, tz)
            }
        }
    }
}
