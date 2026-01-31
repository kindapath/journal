package com.kindaboii.journal.features.entries.impl.presentation.create

sealed interface CreateEntryViewState {
    val data: Data

    data class Data(
        val entryId: String?,
        val title: String,
        val body: String,
        val isSaving: Boolean,
    ) : CreateEntryViewState {
        override val data: Data = this
    }

    object Empty : CreateEntryViewState {
        override val data: Data = Data(
            entryId = null,
            title = "",
            body = "",
            isSaving = false,
        )
    }
}
