package com.kindaboii.journal.theme

interface ThemePreferenceStorage {
    fun getSavedIsDarkTheme(): Boolean?

    fun saveIsDarkTheme(isDarkTheme: Boolean)
}
