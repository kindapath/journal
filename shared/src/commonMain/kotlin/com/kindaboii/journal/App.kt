package com.kindaboii.journal

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.kindaboii.journal.common.ui.LocalJournalThemeController
import com.kindaboii.journal.common.ui.rememberJournalThemeController
import com.kindaboii.journal.navigation.AppNavigation
import com.kindaboii.journal.theme.JournalTheme

@Composable
@Preview
fun App() {
    val systemIsDarkTheme = isSystemInDarkTheme()
    val themeController = rememberJournalThemeController(initialIsDarkTheme = systemIsDarkTheme)

    CompositionLocalProvider(LocalJournalThemeController provides themeController) {
        JournalTheme(darkTheme = themeController.isDarkTheme) {
            AppNavigation()
        }
    }
}