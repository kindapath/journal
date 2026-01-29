package com.kindaboii.journal.features.entries.impl.domain.model

data class Mood(
    val value: Int,
    val emotions: List<String>,
    val influences: List<String>,
)