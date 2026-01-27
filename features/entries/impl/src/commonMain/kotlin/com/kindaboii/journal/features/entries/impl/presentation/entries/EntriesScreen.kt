package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.common.ui.fadingEdges
import com.kindaboii.journal.features.entries.impl.domain.model.Entry
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.koin.compose.koinInject

@Composable
fun EntriesScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onAddEntry: () -> Unit,
) {
    val viewModel: EntriesViewModel = koinInject()
    val uiState by viewModel.uiState

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackgroundBrush()),
    ) {
        val layoutType = layoutTypeFor(maxWidth)
        when (layoutType) {
            EntriesLayoutType.Expanded -> EntriesExpandedScreen(
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme,
                entries = uiState.entries,
                onAddEntry = onAddEntry,
            )
            EntriesLayoutType.Compact -> EntriesCompactScreen(
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme,
                entries = uiState.entries,
                onAddEntry = onAddEntry,
            )
        }
    }
}

@Composable
private fun EntriesExpandedScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    entries: List<Entry>,
    onAddEntry: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        BoxWithConstraints(
            modifier = Modifier
                .weight(10f)
                .widthIn(max = 980.dp),
        ) {
            EntriesScaffold(
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme,
                entries = entries,
                layoutType = EntriesLayoutType.Expanded,
                onAddEntry = onAddEntry,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun EntriesCompactScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    entries: List<Entry>,
    onAddEntry: () -> Unit,
) {
    EntriesScaffold(
        darkTheme = darkTheme,
        onToggleTheme = onToggleTheme,
        entries = entries,
        layoutType = EntriesLayoutType.Compact,
        onAddEntry = onAddEntry,
    )
}

@Composable
private fun EntriesScaffold(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    entries: List<Entry>,
    layoutType: EntriesLayoutType,
    onAddEntry: () -> Unit,
) {
    Scaffold(
        topBar = { EntriesTopBar(darkTheme = darkTheme, onToggleTheme = onToggleTheme) },
        floatingActionButton = { AddEntryFab(onAddEntry) },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
    ) { paddingValues ->
        EntriesContent(
            entries = entries,
            layoutType = layoutType,
            paddingValues = paddingValues,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesTopBar(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "Все записи",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        actions = {
            Text(
                text = if (darkTheme) "Светлая" else "Тёмная",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = MaterialTheme.shapes.small,
                    )
                    .clickable { onToggleTheme() }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
            )
            Text(
                text = "Поиск",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Ещё",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, end = 12.dp),
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Composable
private fun EntriesContent(
    entries: List<Entry>,
    layoutType: EntriesLayoutType,
    paddingValues: PaddingValues,
) {
    val horizontalPadding = if (layoutType == EntriesLayoutType.Expanded) 20.dp else 10.dp
    EntriesListCompact(
        entries = entries,
        horizontalPadding = horizontalPadding,
        paddingValues = paddingValues,
    )
}

@Composable
private fun EntriesListCompact(
    entries: List<Entry>,
    horizontalPadding: Dp,
    paddingValues: PaddingValues,
) {
    SelectionContainer {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(entries, key = { it.id }) { entry ->
                EntryCard(
                    entry = entry,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun EntryCard(
    entry: Entry,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBackgroundBrush())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = entry.summary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 5,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .fillMaxWidth()
                    .fadingEdges(bottom = 20.dp),
            )
            Text(
                text = formatDate(entry.date),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun formatDate(date: LocalDate): String {
    val month = when (date.month) {
        Month.JANUARY -> "января"
        Month.FEBRUARY -> "февраля"
        Month.MARCH -> "марта"
        Month.APRIL -> "апреля"
        Month.MAY -> "мая"
        Month.JUNE -> "июня"
        Month.JULY -> "июля"
        Month.AUGUST -> "августа"
        Month.SEPTEMBER -> "сентября"
        Month.OCTOBER -> "октября"
        Month.NOVEMBER -> "ноября"
        Month.DECEMBER -> "декабря"
    }
    return "${date.day} $month ${date.year}"
}

private fun layoutTypeFor(maxWidth: Dp): EntriesLayoutType {
    return if (maxWidth < 600.dp) EntriesLayoutType.Compact else EntriesLayoutType.Expanded
}

private enum class EntriesLayoutType {
    Compact,
    Expanded,
}

@Composable
private fun AddEntryFab(
    onAddEntry: () -> Unit,
) {
    FloatingActionButton(
        onClick = onAddEntry,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.padding(8.dp),
    ) {
        Text(text = "+", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun appBackgroundBrush(): Brush {
    val colors = listOf(
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        MaterialTheme.colorScheme.background,
    )
    return Brush.verticalGradient(colors = colors)
}

@Composable
private fun cardBackgroundBrush(): Brush {
    val colors = listOf(
        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
    )
    return Brush.linearGradient(colors = colors)
}
