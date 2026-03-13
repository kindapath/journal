package com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kindaboii.journal.features.entries.impl.presentation.components.MoodIndicator
import com.kindaboii.journal.features.entries.impl.presentation.components.moodToColor

@Composable
fun MoodRatingStep(
    moodValue: Long,
    isExpanded: Boolean,
    onMoodValueChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val moodColor = moodToColor(moodValue)
    val targetCircleSize = if (isExpanded) 280.dp else 96.dp
    val targetTopSpacing = if (isExpanded) 32.dp else 8.dp
    val targetSectionSpacing = if (isExpanded) 24.dp else 10.dp
    val targetLabelSpacing = if (isExpanded) 16.dp else 8.dp
    val targetBottomSpacing = if (isExpanded) 24.dp else 14.dp

    val animationSpec = spring<Dp>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow,
    )

    val circleSize by animateDpAsState(targetValue = targetCircleSize, animationSpec = animationSpec)
    val topSpacing by animateDpAsState(targetValue = targetTopSpacing, animationSpec = animationSpec)
    val sectionSpacing by animateDpAsState(targetValue = targetSectionSpacing, animationSpec = animationSpec)
    val labelSpacing by animateDpAsState(targetValue = targetLabelSpacing, animationSpec = animationSpec)
    val bottomSpacing by animateDpAsState(targetValue = targetBottomSpacing, animationSpec = animationSpec)

    val titleStyle = if (isExpanded) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleMedium

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(topSpacing))

        Text(
            text = "Как бы Вы сейчас описали\nсвои ощущения?",
            style = titleStyle,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(sectionSpacing))

        MoodIndicator(
            mood = moodValue,
            indicatorSize = circleSize,
        )
        Spacer(modifier = Modifier.height(labelSpacing))
        Box(modifier = Modifier.width(260.dp), contentAlignment = Alignment.Center) {
            Text(
                text = moodRangeLabel(moodValue),
                style = if (isExpanded) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(sectionSpacing))

        Slider(
            value = moodValue.toFloat(),
            onValueChange = { onMoodValueChange(it.toLong()) },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = moodColor,
                activeTrackColor = moodColor.copy(alpha = 0.6f),
                inactiveTrackColor = Color.White.copy(alpha = 0.15f),
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "ОЧЕНЬ НЕПРИЯТНЫЕ",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = Color.White.copy(alpha = 0.4f),
            )
            Text(
                text = "ОЧЕНЬ ПРИЯТНЫЕ",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = Color.White.copy(alpha = 0.4f),
            )
        }

        Spacer(modifier = Modifier.height(bottomSpacing))
    }
}
