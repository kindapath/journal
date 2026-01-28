package com.kindaboii.journal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import com.kindaboii.journal.navigation.AppNavigation
import com.kindaboii.journal.ui.theme.JournalTheme

@Composable
@Preview
fun App() {
    val isDarkTheme = isSystemInDarkTheme()
    JournalTheme(darkTheme = isDarkTheme) {
        AppNavigation()
    }
}
