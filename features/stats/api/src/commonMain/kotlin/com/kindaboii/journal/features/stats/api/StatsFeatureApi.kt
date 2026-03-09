package com.kindaboii.journal.features.stats.api

import androidx.compose.runtime.Composable

interface StatsFeatureApi {
    @Composable
    fun StatsScreen(onBack: () -> Unit)
}
