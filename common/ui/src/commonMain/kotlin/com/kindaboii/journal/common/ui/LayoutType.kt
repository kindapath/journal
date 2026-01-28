package com.kindaboii.journal.common.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class LayoutType {
    Compact,
    Expanded,
}

@Composable
fun withLayoutType(
    compactMaxWidth: Dp = 600.dp,
    content: @Composable (LayoutType) -> Unit,
) {
    BoxWithConstraints {
        val layoutType = when {
            maxWidth < compactMaxWidth -> LayoutType.Compact
            else -> LayoutType.Expanded
        }
        content(layoutType)
    }
}
