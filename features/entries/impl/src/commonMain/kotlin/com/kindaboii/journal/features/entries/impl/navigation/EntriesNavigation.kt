package com.kindaboii.journal.features.entries.impl.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kindaboii.journal.features.entries.impl.presentation.create.CreateEntryScreen
import com.kindaboii.journal.features.entries.impl.presentation.entries.EntriesScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(EntriesRoute::class, EntriesRoute.serializer())
            subclass(CreateEntryRoute::class, CreateEntryRoute.serializer())
        }
    }
}

@Composable
fun EntriesNavigation(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val backStack = rememberNavBackStack(navConfig, EntriesRoute)

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
                    darkTheme = darkTheme,
                    onToggleTheme = onToggleTheme,
                    onAddEntry = { backStack.add(CreateEntryRoute) },
                )
            }
            entry<CreateEntryRoute> {
                CreateEntryScreen(
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                    onDone = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) },
                )
            }
        },
    )

}
