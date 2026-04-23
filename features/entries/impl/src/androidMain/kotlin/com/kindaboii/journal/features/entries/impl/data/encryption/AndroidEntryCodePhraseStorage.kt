package com.kindaboii.journal.features.entries.impl.data.encryption

import android.content.Context

class AndroidEntryCodePhraseStorage(
    context: Context,
) : EntryCodePhraseStorage {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override val canRememberCodePhrase: Boolean = true

    override fun getCodePhrase(userId: String): String? =
        preferences.getString(storageKey(userId), null)

    override fun saveCodePhrase(userId: String, codePhrase: String) {
        preferences.edit()
            .putString(storageKey(userId), codePhrase)
            .apply()
    }

    override fun clearCodePhrase(userId: String) {
        preferences.edit()
            .remove(storageKey(userId))
            .apply()
    }

    private fun storageKey(userId: String): String =
        "code_phrase_$userId"

    private companion object {
        const val PREFERENCES_NAME = "journal_entry_encryption"
    }
}
