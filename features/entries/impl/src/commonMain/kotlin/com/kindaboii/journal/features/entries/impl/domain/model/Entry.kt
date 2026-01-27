package com.kindaboii.journal.features.entries.impl.domain.model

import kotlinx.datetime.LocalDate

data class Entry(
    val id: String,
    val title: String,
    val summary: String,
    val date: LocalDate,
)


