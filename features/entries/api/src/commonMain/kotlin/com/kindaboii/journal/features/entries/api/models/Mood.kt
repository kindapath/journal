package com.kindaboii.journal.features.entries.api.models

data class Mood(
    val value: Long,
    val emotions: List<String>,
    val influences: List<String>,
)
