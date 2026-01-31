package com.kindaboii.journal.features.entries.impl.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

class CreateEntryViewModel(
    private val repository: EntryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CreateEntryUiState(
            title = "",
            body = "",
            isSaving = false,
        ),
    )
    val uiState: StateFlow<CreateEntryUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun onBodyChange(value: String) {
        _uiState.value = _uiState.value.copy(body = value)
    }

    fun onDone(onSuccess: () -> Unit) {
        if (_uiState.value.isSaving) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            repository.insertEntry(buildEntry())
            _uiState.value = _uiState.value.copy(isSaving = false)
            onSuccess()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun buildEntry(): Entry {
        val nowMillis = Clock.System.now().toEpochMilliseconds()
        val now = Instant.fromEpochMilliseconds(nowMillis)
        val title = _uiState.value.title.trim().ifEmpty { null }
        val body = _uiState.value.body.trim().ifEmpty { null }
        return Entry(
            id = Uuid.random().toString(),
            title = title,
            body = body,
            mood = Mood(
                value = 50,
                emotions = listOf("Calm", "Focused", "Hopeful"),
                influences = listOf("Work", "Rest", "Reflection"),
            ),
            createdAt = now,
            updatedAt = null,
        )
    }
}
