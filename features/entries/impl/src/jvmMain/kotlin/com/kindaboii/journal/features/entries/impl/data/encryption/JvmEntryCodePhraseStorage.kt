package com.kindaboii.journal.features.entries.impl.data.encryption

import java.util.prefs.Preferences

class JvmEntryCodePhraseStorage : EntryCodePhraseStorage {
    private val preferences = Preferences
        .userRoot()
        .node("com/kindaboii/journal/entry_encryption")

    override val canRememberCodePhrase: Boolean = true

    override fun getCodePhrase(userId: String): String? =
        preferences.get(storageKey(userId), null)

    override fun saveCodePhrase(userId: String, codePhrase: String) {
        preferences.put(storageKey(userId), codePhrase)
    }

    override fun clearCodePhrase(userId: String) {
        preferences.remove(storageKey(userId))
    }

    private fun storageKey(userId: String): String =
        "code_phrase_$userId"
}
