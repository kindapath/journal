package com.kindaboii.journal

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.kindaboii.journal.common.ui.LocalJournalThemeController
import com.kindaboii.journal.common.ui.rememberJournalThemeController
import com.kindaboii.journal.navigation.AppNavigation
import com.kindaboii.journal.theme.JournalTheme
import com.kindaboii.journal.theme.ThemePreferenceStorage
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val systemIsDarkTheme = isSystemInDarkTheme()
    val themePreferenceStorage: ThemePreferenceStorage = koinInject()
    val initialIsDarkTheme = themePreferenceStorage.getSavedIsDarkTheme() ?: systemIsDarkTheme
    val themeController = rememberJournalThemeController(
        initialIsDarkTheme = initialIsDarkTheme,
        onThemeChanged = themePreferenceStorage::saveIsDarkTheme,
    )

    CompositionLocalProvider(LocalJournalThemeController provides themeController) {
        JournalTheme(darkTheme = themeController.isDarkTheme) {
            AppNavigation()
        }
    }
}
