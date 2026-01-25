package com.kindaboii.journal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.kindaboii.journal.di.appModule
import com.kindaboii.journal.presentation.entries.EntriesScreen
import com.kindaboii.journal.ui.theme.JournalTheme
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(appModule)
        },
    ) {
        var darkTheme by remember { mutableStateOf(false) }
        JournalTheme(darkTheme = darkTheme) {
            EntriesScreen(
                darkTheme = darkTheme,
                onToggleTheme = { darkTheme = !darkTheme },
            )
        }
    }
}
