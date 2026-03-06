package com.kindaboii.journal.features.profile.impl

import androidx.compose.runtime.Composable
import com.kindaboii.journal.features.profile.api.ProfileFeatureApi
import com.kindaboii.journal.features.profile.impl.presentation.ProfileScreen as ProfileScreenContent

class ProfileFeatureApiImpl : ProfileFeatureApi {
    @Composable
    override fun ProfileScreen(onBack: () -> Unit) {
        ProfileScreenContent(onBack = onBack)
    }
}
