package com.kindaboii.journal.theme

import java.util.prefs.Preferences

class JvmThemePreferenceStorage : ThemePreferenceStorage {
    private val preferences = Preferences
        .userRoot()
        .node("com/kindaboii/journal/theme")

    override fun getSavedIsDarkTheme(): Boolean? =
        preferences.get(STORAGE_KEY, null)?.toBooleanStrictOrNull()

    override fun saveIsDarkTheme(isDarkTheme: Boolean) {
        preferences.put(STORAGE_KEY, isDarkTheme.toString())
    }

    private companion object {
        const val STORAGE_KEY = "is_dark_theme"
    }
}
