package com.kindaboii.journal.features.auth.impl

import androidx.compose.runtime.Composable
import com.kindaboii.journal.AuthFeatureApi
import com.kindaboii.journal.features.auth.impl.presentation.AuthScreen as AuthScreenContent

class AuthFeatureApiImpl : AuthFeatureApi {
    @Composable
    override fun AuthScreen() {
        AuthScreenContent()
    }
}
