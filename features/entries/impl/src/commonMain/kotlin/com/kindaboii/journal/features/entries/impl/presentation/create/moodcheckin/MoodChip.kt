package com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoodChip(
    text: String,
    isSelected: Boolean,
    moodColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    val bg = if (isSelected) moodColor.copy(alpha = 0.45f) else Color.White.copy(alpha = 0.12f)
    val fg = if (isSelected) Color.White else Color.White.copy(alpha = 0.95f)
    val borderColor = if (isSelected) Color.White.copy(alpha = 0.28f) else Color.Transparent

    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = fg,
        modifier = modifier
            .clip(shape)
            .background(color = bg, shape = shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            
            .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}
