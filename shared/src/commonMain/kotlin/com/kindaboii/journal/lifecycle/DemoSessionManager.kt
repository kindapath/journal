package com.kindaboii.journal.lifecycle

import com.kindaboii.journal.domain.AuthService
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.lifecycle.demo.demoEntries
import com.kindaboii.journal.lifecycle.demo.hasCurrentDemoFeed
import kotlinx.coroutines.flow.first

class DemoSessionManager(
    private val authService: AuthService,
    private val entryRepository: EntryRepository,
) {
    private val seededUserIds = mutableSetOf<String>()

    suspend fun ensureDemoData(userId: String) {
        if (!isCurrentDemoUser(userId)) return
        if (!seededUserIds.add(userId)) return

        val existingEntries = entryRepository.getEntries().first()
        if (existingEntries.isNotEmpty() && hasCurrentDemoFeed(existingEntries)) return

        existingEntries.forEach { entry ->
            entryRepository.deleteEntryById(entry.id)
        }

        demoEntries().forEach { entry ->
            entryRepository.insertEntry(entry)
        }
    }

    private fun isCurrentDemoUser(userId: String): Boolean =
        authService.currentUserId() == userId && authService.currentUserEmail().isNullOrBlank()
}
