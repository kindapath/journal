package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.common.ui.fadingEdges
import com.kindaboii.journal.features.entries.impl.domain.model.Entry
import journal.features.entries.impl.generated.resources.Res
import journal.features.entries.impl.generated.resources.icon_add_24
import journal.features.entries.impl.generated.resources.icon_delete_24
import journal.features.entries.impl.generated.resources.icon_edit_note_24
import journal.features.entries.impl.generated.resources.icon_more_horiz_24
import journal.features.entries.impl.generated.resources.icon_search_24

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import journal.features.entries.impl.generated.resources.icon_dark_mode_24
import journal.features.entries.impl.generated.resources.icon_light_mode_24
import journal.features.entries.impl.generated.resources.journal_logo
import org.jetbrains.compose.resources.painterResource
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
            .background(appBackgroundBrush(darkTheme)),
    ) {
        val layoutType = layoutTypeFor(maxWidth)
        when (layoutType) {
            EntriesLayoutType.Expanded -> EntriesExpandedScreen(
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme,
                uiState = uiState,
                onAddEntry = onAddEntry,
            )

            EntriesLayoutType.Compact -> EntriesCompactScreen(
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme,
                uiState = uiState,
                onAddEntry = onAddEntry,
            )
        }
    }
}

@Composable
private fun EntriesExpandedScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    uiState: EntriesUiState,
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
                uiState = uiState,
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
    uiState: EntriesUiState,
    onAddEntry: () -> Unit,
) {
    EntriesScaffold(
        darkTheme = darkTheme,
        onToggleTheme = onToggleTheme,
        uiState = uiState,
        layoutType = EntriesLayoutType.Compact,
        onAddEntry = onAddEntry,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesScaffold(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    uiState: EntriesUiState,
    layoutType: EntriesLayoutType,
    onAddEntry: () -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            EntriesTopBar(
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = { AddEntryFab(onAddEntry) },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
    ) { paddingValues ->
        EntriesContent(
            uiState = uiState,
            layoutType = layoutType,
            paddingValues = paddingValues,
            scrollBehavior = scrollBehavior,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesTopBar(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val menuExpanded = remember { mutableStateOf(false) }
    val menuShape = RoundedCornerShape(16.dp)
    val menuBackground = MaterialTheme.colorScheme.surfaceVariant
    val menuHeightPx = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val menuOffset = with(density) {
        val fallback = 56
        val heightPx = if (menuHeightPx.intValue > 0) menuHeightPx.intValue else fallback
        DpOffset(0.dp, -heightPx.toDp() - 6.dp)
    }
    TopAppBar(
        title = {
            Text(
                text = "Дневник",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        actions = {
            Row(
                modifier = Modifier.padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_search_24),
                    contentDescription = "Поиск",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(32.dp),
                )
                Icon(
                    painter = painterResource(Res.drawable.icon_more_horiz_24),
                    contentDescription = "Ещё",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(32.dp)
                        .clickable { menuExpanded.value = true },
                )
                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false },
                    shape = menuShape,
                    containerColor = menuBackground,
                    offset = menuOffset,
                    modifier = Modifier.onSizeChanged { menuHeightPx.intValue = it.height },
                ) {
                    DropdownMenuItem(
                        text = {
                            MenuItemContent(
                                iconRes = if (darkTheme) Res.drawable.icon_light_mode_24
                                else Res.drawable.icon_dark_mode_24,
                                text = if (darkTheme) "Светлая тема" else "Тёмная тема",
                                onClick = {
                                    menuExpanded.value = false
                                    onToggleTheme()
                                },
                            )
                        },
                        onClick = {
                            menuExpanded.value = false
                            onToggleTheme()
                        },
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
            scrolledContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier.statusBarsPadding(),
        windowInsets = WindowInsets(0, 0, 0, 0),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesContent(
    uiState: EntriesUiState,
    layoutType: EntriesLayoutType,
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    AnimatedContent(
        targetState = uiState,
        label = "entries_state",
    ) { state ->
        when (state) {
            EntriesUiState.Empty -> EntriesEmptyState(paddingValues = paddingValues)
            is EntriesUiState.Content -> EntriesListCompact(
                entries = state.entries,
                paddingValues = paddingValues,
                scrollBehavior = scrollBehavior,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesListCompact(
    entries: List<Entry>,
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    SelectionContainer {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(paddingValues)
                .padding(top = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(entries, key = { it.id }) { entry ->
                EntryCard(
                    entry = entry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
    val menuExpanded = remember { mutableStateOf(false) }
    val summaryLines = remember { mutableIntStateOf(0) }
    val menuShape = RoundedCornerShape(16.dp)
    val menuBackground = MaterialTheme.colorScheme.surfaceVariant
    val menuHeightPx = remember { mutableIntStateOf(0) }
    val menuWidthPx = remember { mutableIntStateOf(0) }
    val cardRightPx = remember { mutableIntStateOf(0) }
    val cardBottomPx = remember { mutableIntStateOf(0) }
    val anchorLeftPx = remember { mutableIntStateOf(0) }
    val anchorBottomPx = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val menuOffset = with(density) {
        val fallbackHeight = 104
        val fallbackWidth = 160
        val heightPx = if (menuHeightPx.intValue > 0) menuHeightPx.intValue else fallbackHeight
        val widthPx = if (menuWidthPx.intValue > 0) menuWidthPx.intValue else fallbackWidth
        val deltaY = cardBottomPx.intValue - anchorBottomPx.intValue
        val deltaX = cardRightPx.intValue - widthPx - anchorLeftPx.intValue
        DpOffset(deltaX.toDp(), (deltaY - heightPx).toDp())
    }

    Card(
        modifier = modifier.onGloballyPositioned { coordinates ->
            val position = coordinates.positionInWindow()
            cardRightPx.intValue = (position.x + coordinates.size.width).toInt()
            cardBottomPx.intValue = (position.y + coordinates.size.height).toInt()
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        ) {
            val title = entry.title?.trim().orEmpty()
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                )
            }
            val summary = entry.summary?.trim().orEmpty()
            if (summary.isNotEmpty()) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 5,
                    overflow = TextOverflow.Clip,
                    modifier = if (summaryLines.intValue >= 5) {
                        Modifier
                            .fillMaxWidth()
                            .fadingEdges(bottom = 80.dp)
                    } else {
                        Modifier.fillMaxWidth()
                    },
                    onTextLayout = { summaryLines.intValue = it.lineCount },
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatDate(entry.date),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Box(
                    modifier = Modifier.wrapContentSize(Alignment.CenterEnd),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_more_horiz_24),
                        contentDescription = "Меню записи",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { menuExpanded.value = true }
                            .onGloballyPositioned { coordinates ->
                                val position = coordinates.positionInWindow()
                                anchorLeftPx.intValue = position.x.toInt()
                                anchorBottomPx.intValue = (position.y + coordinates.size.height).toInt()
                            },
                    )
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false },
                        shape = menuShape,
                        containerColor = menuBackground,
                        offset = menuOffset,
                        modifier = Modifier.onSizeChanged {
                            menuHeightPx.intValue = it.height
                            menuWidthPx.intValue = it.width
                        },
                    ) {
                        DropdownMenuItem(
                            text = {
                                MenuItemContent(
                                    iconRes = Res.drawable.icon_edit_note_24,
                                    text = "Редактировать",
                                    onClick = { menuExpanded.value = false },
                                )
                            },
                            onClick = { menuExpanded.value = false },
                        )
                        DropdownMenuItem(
                            text = {
                                MenuItemContent(
                                    iconRes = Res.drawable.icon_delete_24,
                                    text = "Удалить",
                                    onClick = { menuExpanded.value = false },
                                    color = MaterialTheme.colorScheme.error,
                                )
                            },
                            onClick = { menuExpanded.value = false },
                        )
                    }
                }
            }
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
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(72.dp),
    ) {
        Icon(
            painter = painterResource(Res.drawable.icon_add_24),
            contentDescription = "Добавить запись",
            modifier = Modifier.size(32.dp),
        )
    }
}

@Composable
private fun MenuItemContent(
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    text: String,
    onClick: () -> Unit,
    color: Color? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = color ?: MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = color ?: MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun EntriesEmptyState(
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.journal_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(100.dp),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Это твой дневник",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Сделай свою первую запись.\nНажми на кнопку плюс внизу.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}



@Composable
private fun appBackgroundBrush(
    darkTheme: Boolean,
): Brush {
    val colors = if (darkTheme) {
        listOf(
            Color(0xFF0C0F1F),
            Color(0xFF362644),
            Color(0xFF5F3C69),
        )
    } else {
        listOf(
            Color(0xFFF8F6F5),
            Color(0xFFFAF3F3),
            Color(0xFFF4EFF5),
        )
    }
    return Brush.verticalGradient(colors = colors)
}