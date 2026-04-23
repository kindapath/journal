package com.kindaboii.journal.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
class JournalThemeController(
    initialIsDarkTheme: Boolean,
    private val onThemeChanged: (Boolean) -> Unit = {},
) {
    var isDarkTheme by mutableStateOf(initialIsDarkTheme)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        onThemeChanged(isDarkTheme)
    }
}

@Composable
fun rememberJournalThemeController(
    initialIsDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit = {},
): JournalThemeController =
    remember(initialIsDarkTheme, onThemeChanged) {
        JournalThemeController(
            initialIsDarkTheme = initialIsDarkTheme,
            onThemeChanged = onThemeChanged,
        )
    }

val LocalJournalThemeController = staticCompositionLocalOf<JournalThemeController> {
    error("JournalThemeController is not provided")
}
