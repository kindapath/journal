package com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InfluenceSelectionStep(
    moodValue: Long,
    selectedInfluences: List<String>,
    onToggleInfluence: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    MoodOptionsStepLayout(
        moodValue = moodValue,
        question = "Что оказывает наибольшее влияние?",
        infoText = "Описание контекста может помочь увидеть закономерности в том, что влияет на Ваше самочувствие и ментальное здоровье.",
        optionGroups = influenceGroups.map { it.items },
        selectedItems = selectedInfluences,
        onToggleItem = onToggleInfluence,
        modifier = modifier,
    )
}
