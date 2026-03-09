package com.kindaboii.journal.features.entries.impl.presentation.create

import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class CreateEntryViewModel(
    private val repository: EntryRepository,
) : ViewModel() {
    private val _viewState = MutableStateFlow<CreateEntryViewState>(CreateEntryViewState.Empty)
    val viewState: StateFlow<CreateEntryViewState> = _viewState.asStateFlow()


    private var editingEntry: Entry? = null
    private var loadJob: Job? = null

    fun loadEntry(entryId: String?) {
        entryId ?: return resetToEmpty()

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            runCatching {
                repository.getEntryById(entryId)
            }
                .onSuccess { entry ->
                    editingEntry = entry
                    when (entry) {
                        null -> _viewState.value = CreateEntryViewState.Empty
                        else -> _viewState.update {
                            CreateEntryViewState.Data(
                                entryId = entry.id,
                                title = entry.title.orEmpty(),
                                body = entry.body.orEmpty(),
                                mood = entry.mood ?: defaultMood(),
                                hasMoodCheckIn = entry.mood != null,
                                isSaving = it.data.isSaving,
                            )
                        }
                    }
                }
                .onFailure {
                    editingEntry = null
                    _viewState.value = CreateEntryViewState.Empty
                }
        }
    }

    fun onTitleChange(value: String) {
        _viewState.update { it.data.copy(title = value) }
    }

    fun onBodyChange(value: String) {
        _viewState.update { it.data.copy(body = value) }
    }

    fun onMoodChange(mood: Mood) {
        _viewState.update { it.data.copy(mood = mood, hasMoodCheckIn = true) }
    }

    fun onMoodClear() {
        _viewState.update {
            it.data.copy(
                mood = defaultMood(),
                hasMoodCheckIn = false,
            )
        }
    }

    fun onDone(onSuccess: () -> Unit) {
        val state = _viewState.value.data
        if (state.title.isBlank() && state.body.isBlank() && !state.hasMoodCheckIn) {
            onSuccess()
            return
        }
        if (state.isSaving) return
        viewModelScope.launch {
            _viewState.update { it.data.copy(isSaving = true) }

            val existing = editingEntry

            runCatching {
                when (existing) {
                    null -> repository.insertEntry(buildEntry())
                    else -> repository.updateEntry(buildUpdatedEntry(existing))
                }
            }.onSuccess {
                _viewState.update { it.data.copy(isSaving = false) }
                onSuccess()
            }

        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun buildEntry(): Entry {
        val now = nowInstant()
        val trimmedTitle = _viewState.value.data.title.trim().ifEmpty { null }
        val trimmedBody = _viewState.value.data.body.trim().ifEmpty { null }
        val mood = if (_viewState.value.data.hasMoodCheckIn) _viewState.value.data.mood else null
        return Entry(
            id = Uuid.random().toString(),
            userId = "",
            title = trimmedTitle,
            body = trimmedBody,
            mood = mood,
            createdAt = now,
            updatedAt = null,
            deletedAt = null,
        )
    }

    private fun buildUpdatedEntry(existing: Entry): Entry {
        val now = nowInstant()
        val trimmedTitle = _viewState.value.data.title.trim().ifEmpty { null }
        val trimmedBody = _viewState.value.data.body.trim().ifEmpty { null }
        val mood = if (_viewState.value.data.hasMoodCheckIn) _viewState.value.data.mood else null
        return existing.copy(
            title = trimmedTitle,
            body = trimmedBody,
            mood = mood,
            updatedAt = now,
            deletedAt = null,
        )
    }

    private fun defaultMood(): Mood = Mood(
        value = 50,
        emotions = emptyList(),
        influences = emptyList(),
    )

    private fun nowInstant(): Instant =
        Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())

    private fun resetToEmpty() {
        loadJob?.cancel()
        editingEntry = null
        _viewState.value = CreateEntryViewState.Empty
    }
}
