package com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.features.entries.api.models.Mood
import com.kindaboii.journal.features.entries.impl.presentation.components.moodToColor
import journal.features.entries.impl.generated.resources.Res
import journal.features.entries.impl.generated.resources.icon_arrow_back_24
import journal.features.entries.impl.generated.resources.icon_arrow_forward_24
import journal.features.entries.impl.generated.resources.icon_check_24
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class MoodCheckInStep {
    MOOD_RATING,
    EMOTION_SELECTION,
    INFLUENCE_SELECTION,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodCheckInSheet(
    currentMood: Mood,
    onDismiss: () -> Unit,
    onConfirm: (Mood) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    var step by remember { mutableStateOf(MoodCheckInStep.MOOD_RATING) }
    var moodValue by remember { mutableStateOf(currentMood.value) }
    var selectedEmotions by remember { mutableStateOf(currentMood.emotions) }
    var selectedInfluences by remember { mutableStateOf(currentMood.influences) }

    val moodColor = moodToColor(moodValue)

    val onStepChange = { newStep: MoodCheckInStep ->
        step = newStep
    }

    val onBack = {
        if (step.ordinal > 0) {
            onStepChange(MoodCheckInStep.entries[step.ordinal - 1])
        }
    }

    val onForward = {
        if (step.ordinal < MoodCheckInStep.entries.size - 1) {
            onStepChange(MoodCheckInStep.entries[step.ordinal + 1])
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        sheetMaxWidth = 900.dp,
        containerColor = Color.Transparent,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = Color(0xFF0C0F1F))
                drawRect(color = moodColor.copy(alpha = 0.15f))
                val glowBrush = Brush.radialGradient(
                    colors = listOf(
                        moodColor.copy(alpha = 0.45f),
                        moodColor.copy(alpha = 0.2f),
                        moodColor.copy(alpha = 0.05f),
                        Color.Transparent,
                    ),
                    center = Offset(size.width / 2f, size.height * 0.3f),
                    radius = size.width * 0.9f,
                )
                drawRect(brush = glowBrush)
                val readabilityGradient = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.28f),
                        Color.Black.copy(alpha = 0.52f),
                    ),
                    startY = size.height * 0.42f,
                    endY = size.height,
                )
                drawRect(brush = readabilityGradient)
            }

            Column(modifier = Modifier.fillMaxSize()) {
                MoodCheckInNavBar(
                    currentStep = step,
                    moodColor = moodColor,
                    onBack = onBack,
                    onForward = onForward,
                    onConfirm = {
                        onConfirm(
                            Mood(
                                value = moodValue,
                                emotions = selectedEmotions,
                                influences = selectedInfluences,
                            )
                        )
                    },
                )

                var dragOffset by remember { mutableStateOf(0f) }
                val isExpanded = sheetState.targetValue == SheetValue.Expanded

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(step) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (dragOffset > 100 && step.ordinal > 0) {
                                        onBack()
                                    } else if (dragOffset < -100 && step.ordinal < MoodCheckInStep.entries.size - 1) {
                                        onForward()
                                    }
                                    dragOffset = 0f
                                },
                                onHorizontalDrag = { _, dragAmount ->
                                    dragOffset += dragAmount
                                },
                            )
                        },
                ) {
                    AnimatedContent(
                        targetState = step,
                        transitionSpec = {
                            val direction = if (targetState.ordinal > initialState.ordinal) 1 else -1
                            slideInHorizontally { fullWidth -> direction * fullWidth } togetherWith
                                slideOutHorizontally { fullWidth -> -direction * fullWidth } using
                                SizeTransform(clip = false)
                        },
                        modifier = Modifier.fillMaxSize(),
                    ) { currentStep ->
                        when (currentStep) {
                            MoodCheckInStep.MOOD_RATING -> MoodRatingStep(
                                moodValue = moodValue,
                                isExpanded = isExpanded,
                                onMoodValueChange = { newValue ->
                                    val oldRange = moodValueToRange(moodValue)
                                    val newRange = moodValueToRange(newValue)
                                    moodValue = newValue
                                    if (oldRange != newRange) {
                                        selectedEmotions = emptyList()
                                    }
                                },
                            )

                            MoodCheckInStep.EMOTION_SELECTION -> EmotionSelectionStep(
                                moodValue = moodValue,
                                selectedEmotions = selectedEmotions,
                                onToggleEmotion = { emotion ->
                                    selectedEmotions = if (emotion in selectedEmotions) {
                                        selectedEmotions - emotion
                                    } else {
                                        selectedEmotions + emotion
                                    }
                                },
                            )

                            MoodCheckInStep.INFLUENCE_SELECTION -> InfluenceSelectionStep(
                                moodValue = moodValue,
                                selectedInfluences = selectedInfluences,
                                onToggleInfluence = { influence ->
                                    selectedInfluences = if (influence in selectedInfluences) {
                                        selectedInfluences - influence
                                    } else {
                                        selectedInfluences + influence
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodCheckInNavBar(
    currentStep: MoodCheckInStep,
    moodColor: Color,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (currentStep != MoodCheckInStep.MOOD_RATING) {
            NavButton(
                onClick = onBack,
                iconRes = Res.drawable.icon_arrow_back_24,
                contentDescription = "Назад",
            )
        } else {
            Box(modifier = Modifier.size(48.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MoodCheckInStep.entries.forEach { s ->
                val isActive = s == currentStep
                val dotColor = if (isActive) moodColor else Color.White.copy(alpha = 0.3f)
                val dotSize = if (isActive) 8.dp else 6.dp
                Canvas(modifier = Modifier.size(dotSize)) {
                    drawCircle(color = dotColor)
                }
            }
        }

        if (currentStep == MoodCheckInStep.INFLUENCE_SELECTION) {
            NavButton(
                onClick = onConfirm,
                iconRes = Res.drawable.icon_check_24,
                contentDescription = "Подтвердить",
            )
        } else {
            NavButton(
                onClick = onForward,
                iconRes = Res.drawable.icon_arrow_forward_24,
                contentDescription = "Далее",
            )
        }
    }
}

@Composable
private fun NavButton(
    onClick: () -> Unit,
    iconRes: DrawableResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.White.copy(alpha = 0.2f),
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.08f), CircleShape)
            .border(1.dp, borderColor, CircleShape)
            .clickable(onClick = onClick)
            ,
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(24.dp),
        )
    }
}
