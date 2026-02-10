package com.kindaboii.journal.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kindaboii.journal.features.entries.api.EntriesFeatureApi
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {
    val entriesFeature = koinInject<EntriesFeatureApi>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        entriesFeature.EntriesNavigation()
    }
}
