package com.kindaboii.journal.features.profile.api

import androidx.compose.runtime.Composable

interface ProfileFeatureApi {
    @Composable
    fun ProfileScreen(onBack: () -> Unit)
}
