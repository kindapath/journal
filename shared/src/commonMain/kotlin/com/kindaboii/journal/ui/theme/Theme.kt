package com.kindaboii.journal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors: ColorScheme = lightColorScheme(
    primary = Color(0xFF5E5CE7),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF5755D7),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF8F6F5),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFFAF3F3),
    onSurfaceVariant = Color(0xFF6B6470),
    outline = Color(0xFFB8B1BC),
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = Color(0xFF5E5CE7),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF5F3C69),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF0C0F1F),
    onBackground = Color(0xFFF1ECF6),
    surface = Color(0xFF362644),
    onSurface = Color(0xFFF1ECF6),
    surfaceVariant = Color(0xFF5F3C69),
    onSurfaceVariant = Color(0xFFD6CDE0),
    outline = Color(0xFF787880),
)

@Composable
fun JournalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
