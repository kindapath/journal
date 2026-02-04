package com.kindaboii.journal

import androidx.compose.ui.window.ComposeUIViewController
import com.kindaboii.journal.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}
