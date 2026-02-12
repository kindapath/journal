package com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EmotionSelectionStep(
    moodValue: Long,
    selectedEmotions: List<String>,
    onToggleEmotion: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val emotions = emotionsByMoodRange[moodValueToRange(moodValue)].orEmpty()

    MoodOptionsStepLayout(
        moodValue = moodValue,
        question = "Каким словом можно описать это чувство?",
        infoText = "Если сузить спектр эмоций до одной преобладающей, это поможет понять, как Вы реагируете на происходящее и что Вам может быть сейчас необходимо.",
        optionGroups = listOf(emotions),
        selectedItems = selectedEmotions,
        onToggleItem = onToggleEmotion,
        modifier = modifier,
    )
}
