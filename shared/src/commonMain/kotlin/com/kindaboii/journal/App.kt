package com.kindaboii.journal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import com.kindaboii.journal.di.appModule
import com.kindaboii.journal.navigation.AppNavigation
import com.kindaboii.journal.ui.theme.JournalTheme
import com.kindaboii.journal.settings.UserSettings
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(appModule)
        },
    ) {
        val systemDark = isSystemInDarkTheme()
        var darkTheme by remember {
            mutableStateOf(UserSettings.darkTheme ?: systemDark)
        }
        JournalTheme(darkTheme = darkTheme) {
            AppNavigation(
                darkTheme = darkTheme,
                onToggleTheme = {
                    darkTheme = !darkTheme
                    UserSettings.darkTheme = darkTheme
                },
            )
        }
    }
}
