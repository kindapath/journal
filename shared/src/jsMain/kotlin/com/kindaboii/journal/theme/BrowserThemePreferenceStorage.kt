package com.kindaboii.journal.theme

import kotlinx.browser.localStorage

class BrowserThemePreferenceStorage : ThemePreferenceStorage {
    override fun getSavedIsDarkTheme(): Boolean? =
        localStorage.getItem(STORAGE_KEY)?.toBooleanStrictOrNull()

    override fun saveIsDarkTheme(isDarkTheme: Boolean) {
        localStorage.setItem(STORAGE_KEY, isDarkTheme.toString())
    }

    private companion object {
        const val STORAGE_KEY = "journal.theme.isDark"
    }
}
