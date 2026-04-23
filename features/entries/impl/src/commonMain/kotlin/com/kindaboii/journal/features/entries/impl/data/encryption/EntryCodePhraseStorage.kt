package com.kindaboii.journal.features.entries.impl.data.encryption

interface EntryCodePhraseStorage {
    val canRememberCodePhrase: Boolean

    fun getCodePhrase(userId: String): String?

    fun saveCodePhrase(userId: String, codePhrase: String)

    fun clearCodePhrase(userId: String)
}
