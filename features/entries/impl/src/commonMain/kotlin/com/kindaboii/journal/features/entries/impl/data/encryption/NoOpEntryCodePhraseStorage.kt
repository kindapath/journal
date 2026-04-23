package com.kindaboii.journal.features.entries.impl.data.encryption

class NoOpEntryCodePhraseStorage : EntryCodePhraseStorage {
    override val canRememberCodePhrase: Boolean = false

    override fun getCodePhrase(userId: String): String? = null

    override fun saveCodePhrase(userId: String, codePhrase: String) = Unit

    override fun clearCodePhrase(userId: String) = Unit
}
