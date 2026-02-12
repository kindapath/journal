package com.kindaboii.journal.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import com.kindaboii.journal.features.auth.api.AuthFeatureApi
import com.kindaboii.journal.features.auth.api.AuthState
import com.kindaboii.journal.features.entries.api.EntriesFeatureApi
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {
    val entriesFeature = koinInject<EntriesFeatureApi>()
    val authFeature = koinInject<AuthFeatureApi>()
    val authState by authFeature.authState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        when (authState) {
            AuthState.Loading -> CircularProgressIndicator()
            AuthState.Unauthenticated -> authFeature.AuthScreen()

            is AuthState.Authenticated -> {
                entriesFeature.EntriesNavigation(
                    onSignOut = {
                        scope.launch {
                            authFeature.signOut()
                        }
                    },
                )
            }
        }
    }
}
