package com.kindaboii.journal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors: ColorScheme = lightColorScheme(
    primary = Color(0xFF7B66B8),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF9A89C6),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF4F1F9),
    onBackground = Color(0xFF1E1A28),
    surface = Color(0xFFFCFAFF),
    onSurface = Color(0xFF1E1A28),
    surfaceVariant = Color(0xFFE6E1F0),
    onSurfaceVariant = Color(0xFF4B4556),
    outline = Color(0xFF9A92A8),
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = Color(0xFFC5B6F0),
    onPrimary = Color(0xFF2A203B),
    secondary = Color(0xFFB8A8E2),
    onSecondary = Color(0xFF2B2240),
    background = Color(0xFF15131B),
    onBackground = Color(0xFFE9E3F2),
    surface = Color(0xFF1C1822),
    onSurface = Color(0xFFE9E3F2),
    surfaceVariant = Color(0xFF2A2432),
    onSurfaceVariant = Color(0xFFC9C1D7),
    outline = Color(0xFF8E869C),
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

