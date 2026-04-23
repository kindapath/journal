package com.kindaboii.journal.features.entries.impl.data.encryption

import com.kindaboii.journal.features.entries.api.models.Entry
import dev.whyoleg.cryptography.BinarySize.Companion.bytes
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.algorithms.PBKDF2
import dev.whyoleg.cryptography.algorithms.SHA256
import dev.whyoleg.cryptography.random.CryptographyRandom
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.io.bytestring.ByteString

data class EntryEncryptionState(
    val unlockedUserId: String? = null,
) {
    fun isUnlockedFor(userId: String): Boolean = unlockedUserId == userId
}

class EntryEncryptionService {
    private val provider = CryptographyProvider.Default
    private val _state = MutableStateFlow(EntryEncryptionState())
    private var key: AES.GCM.Key? = null

    val state: StateFlow<EntryEncryptionState> = _state.asStateFlow()

    suspend fun unlock(
        userId: String,
        passphrase: String,
        validationEntries: List<Entry> = emptyList(),
    ): Result<Unit> = runCatching {
        require(userId.isNotBlank()) { "User is required to unlock encryption." }
        require(passphrase.isNotBlank()) { "Encryption passphrase is required." }

        val derivedKey = deriveKey(userId = userId, passphrase = passphrase)
        validateKey(derivedKey, validationEntries)

        key = derivedKey
        _state.value = EntryEncryptionState(unlockedUserId = userId)
    }

    fun lock() {
        key = null
        _state.value = EntryEncryptionState()
    }

    suspend fun encryptEntry(entry: Entry): Entry =
        entry.copy(
            title = entry.title?.let { encryptString(it) },
            body = entry.body?.let { encryptString(it) },
        )

    suspend fun decryptEntry(entry: Entry): Entry =
        entry.copy(
            title = entry.title?.let { decryptString(it) },
            body = entry.body?.let { decryptString(it) },
        )

    fun hasEncryptedFields(entry: Entry): Boolean =
        isEncrypted(entry.title) || isEncrypted(entry.body)

    private suspend fun validateKey(
        candidateKey: AES.GCM.Key,
        entries: List<Entry>,
    ) {
        val encryptedField = entries
            .asSequence()
            .flatMap { sequenceOf(it.title, it.body) }
            .filterNotNull()
            .firstOrNull(::isEncrypted)
            ?: return

        decryptString(value = encryptedField, decryptionKey = candidateKey)
    }

    private suspend fun deriveKey(userId: String, passphrase: String): AES.GCM.Key {
        val salt = "journal:v1:$userId".encodeToByteArray()
        val derivedSecret = provider.get(PBKDF2)
            .secretDerivation(
                digest = SHA256,
                iterations = 210_000,
                outputSize = 32.bytes,
                salt = ByteString(salt),
            )
            .deriveSecret(passphrase.encodeToByteArray())

        return provider.get(AES.GCM)
            .keyDecoder()
            .decodeFromByteArray(AES.Key.Format.RAW, derivedSecret.toByteArray())
    }

    @OptIn(DelicateCryptographyApi::class, ExperimentalEncodingApi::class)
    private suspend fun encryptString(value: String): String {
        if (isEncrypted(value)) return value

        val currentKey = requireKey()
        val nonce = CryptographyRandom.nextBytes(NONCE_BYTES)
        val ciphertext = currentKey.cipher().encryptWithIv(nonce, value.encodeToByteArray())

        return ENCRYPTED_PREFIX +
                Base64.encode(nonce) +
                SEPARATOR +
                Base64.encode(ciphertext)
    }

    @OptIn(DelicateCryptographyApi::class, ExperimentalEncodingApi::class)
    private suspend fun decryptString(
        value: String,
        decryptionKey: AES.GCM.Key = requireKey(),
    ): String {
        if (!isEncrypted(value)) return value

        val parts = value.removePrefix(ENCRYPTED_PREFIX).split(SEPARATOR, limit = 2)
        require(parts.size == 2) { "Invalid encrypted entry field format." }

        val nonce = Base64.decode(parts[0])
        val ciphertext = Base64.decode(parts[1])

        return decryptionKey
            .cipher()
            .decryptWithIv(nonce, ciphertext)
            .decodeToString()
    }

    private fun isEncrypted(value: String?): Boolean =
        value?.startsWith(ENCRYPTED_PREFIX) == true

    private fun requireKey(): AES.GCM.Key =
        key ?: error("Journal encryption is locked.")

    private companion object {
        const val ENCRYPTED_PREFIX = "enc:v1:"
        const val SEPARATOR = ":"
        const val NONCE_BYTES = 12
    }
}
