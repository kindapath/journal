package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
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
import androidx.compose.ui.unit.sp
import com.kindaboii.journal.common.colors.JournalColors
import com.kindaboii.journal.common.ui.ConstrainedContainer
import com.kindaboii.journal.common.ui.LayoutType
import com.kindaboii.journal.common.ui.fadingEdges
import com.kindaboii.journal.common.ui.withLayoutType
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.presentation.components.MoodHeaderBar
import journal.features.entries.impl.generated.resources.Res
import journal.features.entries.impl.generated.resources.icon_add_24
import journal.features.entries.impl.generated.resources.icon_delete_24
import journal.features.entries.impl.generated.resources.icon_edit_note_24
import journal.features.entries.impl.generated.resources.icon_more_horiz_24
import journal.features.entries.impl.generated.resources.journal_logo
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun EntriesScreen(
    onSignOut: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenStats: () -> Unit,
    onAddEntry: () -> Unit,
    onEditEntry: (String) -> Unit,
) {
    val viewModel: EntriesViewModel = koinInject()
    val viewState by viewModel.viewState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackgroundBrush()),
    ) {
        withLayoutType { layoutType ->
            when (layoutType) {
                LayoutType.Expanded -> EntriesExpandedScreen(
                    viewState = viewState,
                    onSignOut = onSignOut,
                    onOpenProfile = onOpenProfile,
                    onOpenStats = onOpenStats,
                    onAddEntry = onAddEntry,
                    onDeleteEntry = viewModel::onDeleteEntry,
                    onEditEntry = onEditEntry,
                )

                LayoutType.Compact -> EntriesCompactScreen(
                    viewState = viewState,
                    onSignOut = onSignOut,
                    onOpenProfile = onOpenProfile,
                    onOpenStats = onOpenStats,
                    onAddEntry = onAddEntry,
                    onDeleteEntry = viewModel::onDeleteEntry,
                    onEditEntry = onEditEntry,
                )
            }
        }
    }
}

@Composable
private fun EntriesExpandedScreen(
    viewState: EntriesViewState,
    onSignOut: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenStats: () -> Unit,
    onAddEntry: () -> Unit,
    onDeleteEntry: (String) -> Unit,
    onEditEntry: (String) -> Unit,
) {
    ConstrainedContainer(maxWidth = 900.dp) {
        EntriesScaffold(
            viewState = viewState,
            layoutType = LayoutType.Expanded,
            onSignOut = onSignOut,
            onOpenProfile = onOpenProfile,
            onOpenStats = onOpenStats,
            onAddEntry = onAddEntry,
            onDeleteEntry = onDeleteEntry,
            onEditEntry = onEditEntry,
        )
    }
}

@Composable
private fun EntriesCompactScreen(
    viewState: EntriesViewState,
    onSignOut: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenStats: () -> Unit,
    onAddEntry: () -> Unit,
    onDeleteEntry: (String) -> Unit,
    onEditEntry: (String) -> Unit,
) {
    EntriesScaffold(
        viewState = viewState,
        layoutType = LayoutType.Compact,
        onSignOut = onSignOut,
        onOpenProfile = onOpenProfile,
        onOpenStats = onOpenStats,
        onAddEntry = onAddEntry,
        onDeleteEntry = onDeleteEntry,
        onEditEntry = onEditEntry,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesScaffold(
    viewState: EntriesViewState,
    layoutType: LayoutType,
    onSignOut: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenStats: () -> Unit,
    onAddEntry: () -> Unit,
    onDeleteEntry: (String) -> Unit,
    onEditEntry: (String) -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            EntriesTopBar(
                scrollBehavior = scrollBehavior,
                onSignOut = onSignOut,
                onOpenProfile = onOpenProfile,
                onOpenStats = onOpenStats,
            )
        },
        floatingActionButton = { AddEntryFab(onAddEntry) },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f),
    ) { paddingValues ->
        EntriesContent(
            viewState = viewState,
            layoutType = layoutType,
            paddingValues = paddingValues,
            scrollBehavior = scrollBehavior,
            onDeleteEntry = onDeleteEntry,
            onEditEntry = onEditEntry,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesTopBar(
    onSignOut: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenStats: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val menuExpanded = remember { mutableStateOf(false) }
    val menuShape = RoundedCornerShape(16.dp)
    val menuBackground = MaterialTheme.colorScheme.surfaceVariant
    val menuHeightPx = remember { mutableIntStateOf(0) }
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
                    painter = painterResource(Res.drawable.icon_more_horiz_24),
                    contentDescription = "Ещё",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(32.dp)
                        .clickable { menuExpanded.value = true }
                        .pointerHoverIcon(PointerIcon.Hand),
                )
                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false },
                    shape = menuShape,
                    containerColor = menuBackground,
                    modifier = Modifier.onSizeChanged { menuHeightPx.intValue = it.height },
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Профиль")
                        },
                        onClick = {
                            menuExpanded.value = false
                            onOpenProfile()
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Статистика")
                        },
                        onClick = {
                            menuExpanded.value = false
                            onOpenStats()
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Экспорт в PDF")
                        },
                        onClick = {
                            menuExpanded.value = false
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Выйти из аккаунта")
                        },
                        onClick = {
                            menuExpanded.value = false
                            onSignOut()
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
        modifier = Modifier.padding(top = 24.dp, bottom = 0.dp),
        windowInsets = TopAppBarDefaults.windowInsets,
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesContent(
    viewState: EntriesViewState,
    layoutType: LayoutType,
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    onDeleteEntry: (String) -> Unit,
    onEditEntry: (String) -> Unit,
) {
    val listState = remember { LazyListState() }

    val listChangeKey = when (viewState) {
        is EntriesViewState.Content -> viewState.entries.firstOrNull()?.id to viewState.entries.size
        else -> null
    }
    LaunchedEffect(listChangeKey) {
        if (listChangeKey != null) {
            listState.scrollToItem(0)
        }
    }

    val maxWidth = if (layoutType == LayoutType.Expanded) 900.dp else Dp.Unspecified
    AnimatedContent(
        targetState = viewState::class,
        label = "entries_state",
    ) {
        when (viewState) {
            EntriesViewState.Loading -> EntriesLoadingState(paddingValues = paddingValues)
            EntriesViewState.Empty -> EntriesEmptyState(paddingValues = paddingValues)
            is EntriesViewState.Content -> EntriesListCompact(
                entries = viewState.entries,
                maxWidth = maxWidth,
                paddingValues = paddingValues,
                scrollBehavior = scrollBehavior,
                onDeleteEntry = onDeleteEntry,
                onEditEntry = onEditEntry,
                listState = listState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesListCompact(
    entries: List<Entry>,
    maxWidth: Dp,
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    listState: LazyListState,
    onDeleteEntry: (String) -> Unit,
    onEditEntry: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(paddingValues),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = maxWidth)
                .padding(top = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(entries, key = { it.id }) { entry ->
                EntryCard(
                    entry = entry,
                    onDeleteEntry = { onDeleteEntry(entry.id) },
                    onEditEntry = { onEditEntry(entry.id) },
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
    onDeleteEntry: () -> Unit,
    onEditEntry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val menuExpanded = remember { mutableStateOf(false) }
    val summaryLines = remember { mutableIntStateOf(0) }
    val cardShape = RoundedCornerShape(16.dp)
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

    val cardModifier = modifier
        .shadow(6.dp, cardShape, clip = false)
        .clip(cardShape)
        .clickable { onEditEntry() }
        .pointerHoverIcon(PointerIcon.Hand)
        .onGloballyPositioned { coordinates ->
            val position = coordinates.positionInWindow()
            cardRightPx.intValue = (position.x + coordinates.size.width).toInt()
            cardBottomPx.intValue = (position.y + coordinates.size.height).toInt()
        }

    Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            entry.mood?.let { mood ->
                MoodHeaderBar(
                    mood = mood.value,
                    emotions = mood.emotions,
                    influences = mood.influences,
                    time = entry.createdAt,
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp),
                )
            }
            val title = entry.title?.trim().orEmpty()
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(bottom = 4.dp, start = 16.dp, end = 16.dp),
                )
            }
            val body = entry.body?.trim().orEmpty()
            if (body.isNotEmpty()) {
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 18.sp),
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 5,
                    overflow = TextOverflow.Clip,
                    modifier = if (summaryLines.intValue >= 5) {
                        Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .fadingEdges(bottom = 40.dp)
                    } else {
                        Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    },
                    onTextLayout = { summaryLines.intValue = it.lineCount },
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp, start = 4.dp, end = 4.dp),
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatDate(entry.createdAt),
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
                                anchorBottomPx.intValue =
                                    (position.y + coordinates.size.height).toInt()
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
                                )
                            },
                            onClick = {
                                menuExpanded.value = false
                                onEditEntry()
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                MenuItemContent(
                                    iconRes = Res.drawable.icon_delete_24,
                                    text = "Удалить",
                                    color = MaterialTheme.colorScheme.error,
                                )
                            },
                            onClick = {
                                menuExpanded.value = false
                                onDeleteEntry()
                            },
                        )
                    }
                }
            }
        }
    }
}

private fun formatDate(date: Instant): String {
    val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val month = when (localDate.month) {
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
    return "${localDate.day} $month ${localDate.year}"
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
    iconRes: DrawableResource,
    text: String,
    color: Color? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            text = "Еще нет записей",
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
private fun EntriesLoadingState(
    paddingValues: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun appBackgroundBrush(): Brush {
    val colors = if (isSystemInDarkTheme()) {
        listOf(
            JournalColors.BackgroundDark,
            JournalColors.SurfaceDark,
            JournalColors.SecondaryDark,
        )
    } else {
        listOf(
            JournalColors.BackgroundLight,
            JournalColors.SurfaceVariantLight,
            JournalColors.SurfaceTintLight,
        )
    }
    return Brush.verticalGradient(colors = colors)
}
