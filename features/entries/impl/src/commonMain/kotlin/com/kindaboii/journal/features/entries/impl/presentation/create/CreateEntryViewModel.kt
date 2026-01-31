package com.kindaboii.journal.features.entries.impl.presentation.create

import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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
                repository.getEntryById(entryId).firstOrNull()
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

    fun onDone(onSuccess: () -> Unit) {
        if (_viewState.value.data.isSaving) return
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
        val (title, body) = trimmedTitleBody()
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

    private fun buildUpdatedEntry(existing: Entry): Entry {
        val now = nowInstant()
        val (title, body) = trimmedTitleBody()
        return existing.copy(
            title = title,
            body = body,
            updatedAt = now,
        )
    }

    private fun trimmedTitleBody(): Pair<String?, String?> {
        val state = _viewState.value.data
        val title = state.title.trim().ifEmpty { null }
        val body = state.body.trim().ifEmpty { null }
        return title to body
    }

    private fun nowInstant(): Instant =
        Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())

    private fun resetToEmpty() {
        loadJob?.cancel()
        editingEntry = null
        _viewState.value = CreateEntryViewState.Empty
    }
}
