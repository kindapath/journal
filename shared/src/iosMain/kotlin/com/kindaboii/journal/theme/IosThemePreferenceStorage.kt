package com.kindaboii.journal.theme

import platform.Foundation.NSUserDefaults

class IosThemePreferenceStorage : ThemePreferenceStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun getSavedIsDarkTheme(): Boolean? =
        if (defaults.objectForKey(STORAGE_KEY) == null) null else defaults.boolForKey(STORAGE_KEY)

    override fun saveIsDarkTheme(isDarkTheme: Boolean) {
        defaults.setBool(isDarkTheme, STORAGE_KEY)
    }

    private companion object {
        const val STORAGE_KEY = "journal.theme.isDark"
    }
}
