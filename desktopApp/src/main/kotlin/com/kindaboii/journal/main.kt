package com.kindaboii.journal

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kindaboii.journal.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Journal",
    ) {
        App()
    }
}
