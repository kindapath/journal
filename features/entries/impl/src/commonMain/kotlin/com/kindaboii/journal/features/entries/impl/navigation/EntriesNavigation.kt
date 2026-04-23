package com.kindaboii.journal.features.entries.impl.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.TransformOrigin
import com.kindaboii.journal.domain.AuthService
import com.kindaboii.journal.domain.AuthState
import com.kindaboii.journal.features.entries.impl.data.encryption.EntryEncryptionService
import com.kindaboii.journal.features.entries.impl.data.repository.EntryRepository
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kindaboii.journal.features.entries.impl.presentation.create.CreateEntryScreen
import com.kindaboii.journal.features.entries.impl.presentation.entries.EntriesScreen
import com.kindaboii.journal.features.entries.impl.presentation.unlock.EntryEncryptionUnlockScreen
import com.kindaboii.journal.features.profile.api.ProfileFeatureApi
import com.kindaboii.journal.features.stats.api.StatsFeatureApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.koinInject

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(EntriesRoute::class, EntriesRoute.serializer())
            subclass(CreateEntryRoute::class, CreateEntryRoute.serializer())
            subclass(ProfileRoute::class, ProfileRoute.serializer())
            subclass(StatsRoute::class, StatsRoute.serializer())
        }
    }
}

@Composable
fun EntriesNavigation(
    onSignOut: () -> Unit,
) {
    val profileFeature = koinInject<ProfileFeatureApi>()
    val statsFeature = koinInject<StatsFeatureApi>()
    val authService = koinInject<AuthService>()
    val encryptionService = koinInject<EntryEncryptionService>()
    val repository = koinInject<EntryRepository>()
    val authState by authService.authState.collectAsState()
    val encryptionState by encryptionService.state.collectAsState()
    val backStack = rememberNavBackStack(navConfig, EntriesRoute)
    val authenticated = authState as? AuthState.Authenticated
    val userId = authenticated?.userId
    val signOutAndLock = {
        runCatching { repository.forgetCodePhrase() }
        encryptionService.lock()
        onSignOut()
    }

    if (userId != null && !encryptionState.isUnlockedFor(userId)) {
        EntryEncryptionUnlockScreen(
            onSignOut = signOutAndLock,
        )
        return
    }

    NavDisplay(
        backStack = backStack,
        transitionSpec = {
            scaleIn(
                initialScale = 0.92f,
                transformOrigin = TransformOrigin.Center,
            ) + fadeIn() togetherWith
                    scaleOut(
                        targetScale = 0.98f,
                        transformOrigin = TransformOrigin.Center,
                    ) + fadeOut()
        },
        popTransitionSpec = {
            scaleIn(
                initialScale = 0.98f,
                transformOrigin = TransformOrigin.Center,
            ) + fadeIn() togetherWith
                    scaleOut(
                        targetScale = 0.92f,
                        transformOrigin = TransformOrigin.Center,
                    ) + fadeOut()
        },
        predictivePopTransitionSpec = {
            scaleIn(
                initialScale = 0.98f,
                transformOrigin = TransformOrigin.Center,
            ) + fadeIn() togetherWith
                    scaleOut(
                        targetScale = 0.92f,
                        transformOrigin = TransformOrigin.Center,
                    ) + fadeOut()
        },
        entryProvider = entryProvider {
            entry<EntriesRoute> {
                EntriesScreen(
                    onSignOut = signOutAndLock,
                    onOpenProfile = {
                        if (backStack.lastOrNull() != ProfileRoute) {
                            backStack.add(ProfileRoute)
                        }
                    },
                    onOpenStats = {
                        if (backStack.lastOrNull() != StatsRoute) {
                            backStack.add(StatsRoute)
                        }
                    },
                    onAddEntry = {
                        if (backStack.lastOrNull() != CreateEntryRoute()) {
                            backStack.add(CreateEntryRoute())
                        }
                    },
                    onEditEntry = { entryId ->
                        val route = CreateEntryRoute(entryId = entryId)
                        if (backStack.lastOrNull() != route) {
                            backStack.add(route)
                        }
                    },
                )
            }
            entry<CreateEntryRoute> { route ->
                CreateEntryScreen(
                    entryId = route.entryId,
                    onBack = { if (backStack.size > 1) backStack.removeLast() },
                    onDone = { if (backStack.size > 1) backStack.removeLast() },
                )
            }
            entry<ProfileRoute> {
                profileFeature.ProfileScreen(
                    onBack = { if (backStack.size > 1) backStack.removeLast() },
                )
            }
            entry<StatsRoute> {
                statsFeature.StatsScreen(
                    onBack = { if (backStack.size > 1) backStack.removeLast() },
                )
            }
        },
    )
}
