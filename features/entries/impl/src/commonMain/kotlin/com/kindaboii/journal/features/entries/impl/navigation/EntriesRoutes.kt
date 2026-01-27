package com.kindaboii.journal.features.entries.impl.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object EntriesRoute : NavKey

@Serializable
data object CreateEntryRoute : NavKey