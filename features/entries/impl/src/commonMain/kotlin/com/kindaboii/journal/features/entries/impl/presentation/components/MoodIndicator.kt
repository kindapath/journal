package com.kindaboii.journal.features.entries.impl.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import com.kindaboii.journal.common.colors.JournalColors
import kotlin.math.min

@Composable
fun MoodIndicator(
    mood: Long,
    indicatorSize: Dp,
    modifier: Modifier = Modifier,
) {
    val centerColor = moodToColor(mood)
    val rimColor = moodToColor(mood + 12)

    Canvas(modifier = modifier.size(indicatorSize)) {
        val radius = min(size.width, size.height) / 2f
        val glowRadius = radius * 1.2f
        val glowBrush = Brush.radialGradient(
            colors = listOf(
                centerColor.copy(alpha = 0.95f),
                centerColor.copy(alpha = 0.45f),
                Color.Transparent,
            ),
            center = Offset(radius, radius),
            radius = glowRadius,
        )
        val softHaloRadius = radius * 1.5f
        val softHaloBrush = Brush.radialGradient(
            colors = listOf(
                centerColor.copy(alpha = 0.45f),
                Color.Transparent,
            ),
            center = Offset(radius, radius),
            radius = softHaloRadius,
        )
        val coreRadius = radius
        val coreBrush = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.5f),
                centerColor.copy(alpha = 0.5f),
                centerColor.copy(alpha = 0.2f),
                Color.Transparent,
            ),
            center = Offset(radius, radius),
            radius = coreRadius * 2f,
        )
        val brush = Brush.radialGradient(
            colors = listOf(
                centerColor,
                lerp(centerColor, rimColor, 0.55f),
                rimColor.copy(alpha = 0.45f),
                rimColor.copy(alpha = 0.2f),
            ),
            center = Offset(radius, radius),
            radius = radius,
        )
        drawCircle(brush = softHaloBrush, radius = softHaloRadius, center = Offset(radius, radius))
        drawCircle(brush = glowBrush, radius = glowRadius, center = Offset(radius, radius))
        drawCircle(brush = brush, radius = radius, center = Offset(radius, radius))
        drawCircle(brush = coreBrush, radius = coreRadius, center = Offset(radius, radius))
    }
}

internal fun moodToColor(
    mood: Long,
): Color {
    val clamped = mood.coerceIn(0, 100)
    val t = clamped / 100f
    val palette = listOf(
        0f to JournalColors.MoodVeryUnpleasant,
        0.16f to JournalColors.MoodUnpleasant,
        0.33f to JournalColors.MoodSlightlyUnpleasant,
        0.5f to JournalColors.MoodNeutral,
        0.66f to JournalColors.MoodSlightlyPleasant,
        0.83f to JournalColors.MoodPleasant,
        1f to JournalColors.MoodVeryPleasant,
    )
    for (i in 0 until palette.lastIndex) {
        val (t0, c0) = palette[i]
        val (t1, c1) = palette[i + 1]
        if (t <= t1) {
            val localT = if (t1 - t0 == 0f) 0f else (t - t0) / (t1 - t0)
            return lerp(c0, c1, localT)
        }
    }
    return palette.last().second
}
