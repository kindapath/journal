package com.kindaboii.journal.theme

import android.content.Context

class AndroidThemePreferenceStorage(
    context: Context,
) : ThemePreferenceStorage {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun getSavedIsDarkTheme(): Boolean? =
        preferences.getString(STORAGE_KEY, null)?.toBooleanStrictOrNull()

    override fun saveIsDarkTheme(isDarkTheme: Boolean) {
        preferences.edit()
            .putString(STORAGE_KEY, isDarkTheme.toString())
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "journal_theme"
        const val STORAGE_KEY = "is_dark_theme"
    }
}
