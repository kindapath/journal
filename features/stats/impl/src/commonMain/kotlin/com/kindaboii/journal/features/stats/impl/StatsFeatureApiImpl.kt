package com.kindaboii.journal.features.stats.impl

import androidx.compose.runtime.Composable
import com.kindaboii.journal.features.stats.api.StatsFeatureApi
import com.kindaboii.journal.features.stats.impl.presentation.StatsScreen

class StatsFeatureApiImpl : StatsFeatureApi {
    @Composable
    override fun StatsScreen(onBack: () -> Unit) {
        StatsScreen(onBack = onBack)
    }
}
