package com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin

import androidx.compose.animation.Crossfade
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.features.entries.impl.presentation.components.MoodIndicator
import com.kindaboii.journal.features.entries.impl.presentation.components.moodToColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MoodOptionsStepLayout(
    moodValue: Long,
    question: String,
    infoText: String,
    optionGroups: List<List<String>>,
    selectedItems: List<String>,
    onToggleItem: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val moodColor = moodToColor(moodValue)
    val scrollState = rememberScrollState()
    var infoVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            MoodIndicator(
                mood = moodValue,
                indicatorSize = 64.dp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Crossfade(targetState = moodRangeLabel(moodValue)) { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f), CircleShape)
                    .clickable { infoVisible = !infoVisible }
                    ,
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "i",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.14f),
        )
        AnimatedVisibility(
            visible = infoVisible,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = infoText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.62f),
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))

        optionGroups.forEachIndexed { index, group ->
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                group.forEach { option ->
                    MoodChip(
                        text = option,
                        isSelected = option in selectedItems,
                        moodColor = moodColor,
                        onClick = { onToggleItem(option) },
                    )
                }
            }
            if (index < optionGroups.lastIndex) {
                Spacer(modifier = Modifier.height(22.dp))
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
