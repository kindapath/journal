package com.kindaboii.journal.common.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.fadingEdges(
    top: Dp = 0.dp,
    bottom: Dp = 0.dp,
): Modifier {
    if (top == 0.dp && bottom == 0.dp) return this
    return this
        .graphicsLayer { alpha = 0.99f }
        .drawWithContent {
            drawContent()
            if (top > 0.dp) {
                val topHeight = top.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = topHeight,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }
            if (bottom > 0.dp) {
                val bottomHeight = bottom.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startY = size.height - bottomHeight,
                        endY = size.height,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }
        }
}
