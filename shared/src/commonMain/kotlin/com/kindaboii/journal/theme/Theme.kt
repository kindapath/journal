package com.kindaboii.journal.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.kindaboii.journal.common.colors.JournalColors

private val LightColors: ColorScheme = lightColorScheme(
    primary = JournalColors.Primary,
    onPrimary = JournalColors.PrimaryOn,
    secondary = JournalColors.SecondaryLight,
    onSecondary = JournalColors.PrimaryOn,
    background = JournalColors.BackgroundLight,
    onBackground = JournalColors.BackgroundOnLight,
    surface = JournalColors.SurfaceLight,
    onSurface = JournalColors.SurfaceOnLight,
    surfaceVariant = JournalColors.SurfaceVariantLight,
    onSurfaceVariant = JournalColors.SurfaceVariantOnLight,
    outline = JournalColors.OutlineLight,
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = JournalColors.Primary,
    onPrimary = JournalColors.PrimaryOn,
    secondary = JournalColors.SecondaryDark,
    onSecondary = JournalColors.PrimaryOn,
    background = JournalColors.BackgroundDark,
    onBackground = JournalColors.BackgroundOnDark,
    surface = JournalColors.SurfaceDark,
    onSurface = JournalColors.SurfaceOnDark,
    surfaceVariant = JournalColors.SurfaceVariantDark,
    onSurfaceVariant = JournalColors.SurfaceVariantOnDark,
    outline = JournalColors.OutlineDark,
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
