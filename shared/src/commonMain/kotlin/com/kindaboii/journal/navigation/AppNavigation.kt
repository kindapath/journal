package com.kindaboii.journal.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kindaboii.journal.features.entries.impl.navigation.EntriesNavigation

@Composable
fun AppNavigation(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        EntriesNavigation(
            darkTheme = darkTheme,
            onToggleTheme = onToggleTheme,
        )
    }
}
