package com.kindaboii.journal.features.entries.impl.domain.models

data class Mood(
    val value: Long,
    val emotions: List<String>,
    val influences: List<String>,
)