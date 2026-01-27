package com.kindaboii.journal.navigation

import androidx.compose.runtime.Composable
import com.kindaboii.journal.features.entries.impl.navigation.EntriesNavigation

@Composable
fun AppNavigation(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    EntriesNavigation(
        darkTheme = darkTheme,
        onToggleTheme = onToggleTheme,
    )
}
