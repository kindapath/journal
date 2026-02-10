package com.kindaboii.journal.features.entries.impl.presentation.create

import com.kindaboii.journal.features.entries.api.models.Mood

sealed interface CreateEntryViewState {
    val data: Data

    data class Data(
        val entryId: String?,
        val title: String,
        val body: String,
        val mood: Mood,
        val isSaving: Boolean,
    ) : CreateEntryViewState {
        override val data: Data = this
    }

    object Empty : CreateEntryViewState {
        override val data: Data = Data(
            entryId = null,
            title = "",
            body = "",
            mood = Mood(
                value = 50,
                emotions = listOf("Calm", "Focused", "Hopeful"), // TODO: replace with actual feature
                influences = listOf("Work", "Rest", "Reflection"),
            ),
            isSaving = false,
        )
    }
}
