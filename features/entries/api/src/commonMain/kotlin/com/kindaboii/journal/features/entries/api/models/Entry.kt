package com.kindaboii.journal.features.entries.api.models

import kotlin.time.Instant

data class Entry(
    val id: String,
    val title: String?,
    val body: String?,
    val mood: Mood?,
    val createdAt: Instant,
    val updatedAt: Instant?,
)
