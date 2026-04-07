package com.kindaboii.journal.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
class JournalThemeController(initialIsDarkTheme: Boolean) {
    var isDarkTheme by mutableStateOf(initialIsDarkTheme)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}

@Composable
fun rememberJournalThemeController(initialIsDarkTheme: Boolean): JournalThemeController =
    remember(initialIsDarkTheme) {
        JournalThemeController(initialIsDarkTheme = initialIsDarkTheme)
    }

val LocalJournalThemeController = staticCompositionLocalOf<JournalThemeController> {
    error("JournalThemeController is not provided")
}