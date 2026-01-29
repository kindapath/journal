package com.kindaboii.journal.features.entries.impl.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant  

@Composable
fun MoodHeaderBar(
    mood: Int,
    emotions: List<String>,
    influences: List<String>,
    time: Instant,
    modifier: Modifier = Modifier,
    height: Dp = 80.dp,
) {
    val baseColor = moodToColor(mood)
    val background = Brush.verticalGradient(
        listOf(
            baseColor.copy(alpha = 0.6f),
            Color.Black.copy(alpha = 0.6f),
        ),
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(12.dp))
            .background(background, RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MoodIndicator(
            mood = mood,
            indicatorSize = height - 24.dp,
        )
        Column(
            modifier = Modifier.padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            val emotionsText = emotions.take(2).joinToString(", ")
            val influencesText = influences.take(2).let { items ->
                if (influences.size > 2) "${items.joinToString(", ")} и другие"
                else items.joinToString(", ")
            }
            val timeText = formatTime(time)
            Text(
                text = emotionsText,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                ),
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.72f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = influencesText,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                ),
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.52f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Эмоция · $timeText",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                ),
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.42f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun formatTime(time: Instant): String {
    val local = time.toLocalDateTime(TimeZone.currentSystemDefault())
    val hh = local.hour.toString().padStart(2, '0')
    val mm = local.minute.toString().padStart(2, '0')
    return "$hh:$mm"
}
