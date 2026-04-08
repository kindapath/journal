package com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.features.entries.api.models.Mood
import journal.features.entries.impl.generated.resources.Res
import journal.features.entries.impl.generated.resources.mindfulness_24px
import journal.features.entries.impl.generated.resources.psychiatry_24px
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Instant

@Composable
fun CollapsedMoodBar(
    mood: Mood,
    time: Instant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
            ,
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.mindfulness_24px),
            contentDescription = "Mindfulness",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(32.dp),
        )
    }
}
