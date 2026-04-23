package com.kindaboii.journal.features.entries.impl.data.encryption

import kotlinx.browser.localStorage

class BrowserEntryCodePhraseStorage : EntryCodePhraseStorage {
    override val canRememberCodePhrase: Boolean = true

    override fun getCodePhrase(userId: String): String? =
        localStorage.getItem(storageKey(userId))

    override fun saveCodePhrase(userId: String, codePhrase: String) {
        localStorage.setItem(storageKey(userId), codePhrase)
    }

    override fun clearCodePhrase(userId: String) {
        localStorage.removeItem(storageKey(userId))
    }

    private fun storageKey(userId: String): String =
        "journal.codePhrase.$userId"
}
