package com.kindaboii.journal.features.entries.impl.presentation.entries

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.kindaboii.journal.common.ui.LocalJournalThemeController
import com.kindaboii.journal.common.ui.fadingEdges
import com.kindaboii.journal.common.ui.withLayoutType
import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.impl.presentation.components.MoodHeaderBar
import journal.features.entries.impl.generated.resources.Res
import journal.features.entries.impl.generated.resources.icon_add_24
import journal.features.entries.impl.generated.resources.icon_arrow_drop_up_24px
import journal.features.entries.impl.generated.resources.icon_dark_mode_24
import journal.features.entries.impl.generated.resources.icon_date_range_24px
import journal.features.entries.impl.generated.resources.icon_delete_24
import journal.features.entries.impl.generated.resources.icon_cancel_24px
import journal.features.entries.impl.generated.resources.icon_file_export_24px
import journal.features.entries.impl.generated.resources.icon_edit_note_24
import journal.features.entries.impl.generated.resources.icon_export_24
import journal.features.entries.impl.generated.resources.icon_light_mode_24
import journal.features.entries.impl.generated.resources.icon_leaderboard_24px
import journal.features.entries.impl.generated.resources.icon_logout_24px
import journal.features.entries.impl.generated.resources.icon_more_horiz_24
import journal.features.entries.impl.generated.resources.icon_person_24px
import journal.features.entries.impl.generated.resources.icon_search_24
import journal.features.entries.impl.generated.resources.journal_logo
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
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
                    onExportEntry = viewModel::onExportEntry,
                    onExportAll = viewModel::onExportAll,
                    onApplyDateFilter = viewModel::onApplyDateFilter,
                    onClearDateFilter = viewModel::onClearDateFilter,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onClearSearch = viewModel::onClearSearch,
                )

                LayoutType.Compact -> EntriesCompactScreen(
                    viewState = viewState,
                    onSignOut = onSignOut,
                    onOpenProfile = onOpenProfile,
                    onOpenStats = onOpenStats,
                    onAddEntry = onAddEntry,
                    onDeleteEntry = viewModel::onDeleteEntry,
                    onEditEntry = onEditEntry,
                    onExportEntry = viewModel::onExportEntry,
                    onExportAll = viewModel::onExportAll,
                    onApplyDateFilter = viewModel::onApplyDateFilter,
                    onClearDateFilter = viewModel::onClearDateFilter,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onClearSearch = viewModel::onClearSearch,
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
    onExportEntry: (Entry) -> Unit,
    onExportAll: () -> Unit,
    onApplyDateFilter: (LocalDate?, LocalDate?) -> Unit,
    onClearDateFilter: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
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
            onExportEntry = onExportEntry,
            onExportAll = onExportAll,
            onApplyDateFilter = onApplyDateFilter,
            onClearDateFilter = onClearDateFilter,
            onSearchQueryChange = onSearchQueryChange,
            onClearSearch = onClearSearch,
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
    onExportEntry: (Entry) -> Unit,
    onExportAll: () -> Unit,
    onApplyDateFilter: (LocalDate?, LocalDate?) -> Unit,
    onClearDateFilter: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
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
        onExportEntry = onExportEntry,
        onExportAll = onExportAll,
        onApplyDateFilter = onApplyDateFilter,
        onClearDateFilter = onClearDateFilter,
        onSearchQueryChange = onSearchQueryChange,
        onClearSearch = onClearSearch,
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
    onExportEntry: (Entry) -> Unit,
    onExportAll: () -> Unit,
    onApplyDateFilter: (LocalDate?, LocalDate?) -> Unit,
    onClearDateFilter: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val dateFilter = viewState.dateFilter()
    val searchFilter = viewState.searchFilter()

    Scaffold(
        topBar = {
            EntriesTopBar(
                scrollBehavior = scrollBehavior,
                onSignOut = onSignOut,
                onOpenProfile = onOpenProfile,
                onOpenStats = onOpenStats,
                onExportAll = onExportAll,
                dateFilter = dateFilter,
                searchFilter = searchFilter,
                onApplyDateFilter = onApplyDateFilter,
                onClearDateFilter = onClearDateFilter,
                onSearchQueryChange = onSearchQueryChange,
                onClearSearch = onClearSearch,
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
            onExportEntry = onExportEntry,
            onClearDateFilter = onClearDateFilter,
            onClearSearch = onClearSearch,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesTopBar(
    onSignOut: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenStats: () -> Unit,
    onExportAll: () -> Unit,
    dateFilter: EntriesDateRangeFilter,
    searchFilter: EntriesSearchFilter,
    onApplyDateFilter: (LocalDate?, LocalDate?) -> Unit,
    onClearDateFilter: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val themeController = LocalJournalThemeController.current
    val menuExpanded = remember { mutableStateOf(false) }
    val filterDialogVisible = remember { mutableStateOf(false) }
    val searchVisible = rememberSaveable { mutableStateOf(searchFilter.isActive) }
    val menuShape = RoundedCornerShape(16.dp)
    val menuBackground = MaterialTheme.colorScheme.surfaceVariant
    val menuHeightPx = remember { mutableIntStateOf(0) }

    LaunchedEffect(searchFilter.isActive) {
        if (searchFilter.isActive) {
            searchVisible.value = true
        }
    }

    if (filterDialogVisible.value) {
        EntriesDateRangeFilterDialog(
            currentFilter = dateFilter,
            onDismiss = { filterDialogVisible.value = false },
            onApply = { filter ->
                onApplyDateFilter(filter.from, filter.to)
                filterDialogVisible.value = false
            },
            onClear = {
                onClearDateFilter()
                filterDialogVisible.value = false
            },
        )
    }

    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Дневник",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            actions = {
                Row(
                    modifier = Modifier.padding(end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val searchHoverSource = remember { MutableInteractionSource() }
                    val searchHovered by searchHoverSource.collectIsHoveredAsState()
                    val filterHoverSource = remember { MutableInteractionSource() }
                    val filterHovered by filterHoverSource.collectIsHoveredAsState()
                    val moreHoverSource = remember { MutableInteractionSource() }
                    val moreHovered by moreHoverSource.collectIsHoveredAsState()

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                color = when {
                                    searchVisible.value || searchFilter.isActive -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                                    searchHovered -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                    else -> Color.Transparent
                                },
                                shape = CircleShape,
                            )
                            .hoverable(searchHoverSource)
                            .clickable(interactionSource = searchHoverSource, indication = null) {
                                if (searchVisible.value) {
                                    if (searchFilter.isActive) {
                                        onClearSearch()
                                    }
                                    searchVisible.value = false
                                } else {
                                    searchVisible.value = true
                                }
                            }
                            .pointerHoverIcon(PointerIcon.Hand),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_search_24),
                            contentDescription = if (searchVisible.value || searchFilter.isActive) "Закрыть поиск" else "Поиск",
                            tint = if (searchVisible.value || searchFilter.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                color = when {
                                    dateFilter.isActive -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                                    filterHovered -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                    else -> Color.Transparent
                                },
                                shape = CircleShape,
                            )
                            .hoverable(filterHoverSource)
                            .clickable(interactionSource = filterHoverSource, indication = null) { filterDialogVisible.value = true }
                            .pointerHoverIcon(PointerIcon.Hand),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_date_range_24px),
                            contentDescription = "Фильтр по датам",
                            tint = if (dateFilter.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (moreHovered) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else Color.Transparent,
                                shape = CircleShape,
                            )
                            .hoverable(moreHoverSource)
                            .clickable(interactionSource = moreHoverSource, indication = null) { menuExpanded.value = true }
                            .pointerHoverIcon(PointerIcon.Hand),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_more_horiz_24),
                            contentDescription = "???",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false },
                        shape = menuShape,
                        containerColor = menuBackground,
                        modifier = Modifier.onSizeChanged { menuHeightPx.intValue = it.height },
                    ) {
                        DropdownMenuItem(
                            text = {
                                MenuItemContent(
                                    iconRes = if (themeController.isDarkTheme) Res.drawable.icon_light_mode_24 else Res.drawable.icon_dark_mode_24,
                                    text = if (themeController.isDarkTheme) "Светлая тема" else "Тёмная тема",
                                )
                            },
                            onClick = {
                                menuExpanded.value = false
                                themeController.toggleTheme()
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                MenuItemContent(
                                    iconRes = Res.drawable.icon_person_24px,
                                    text = "Профиль",
                                )
                            },
                            onClick = {
                                menuExpanded.value = false
                                onOpenProfile()
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                MenuItemContent(
                                    iconRes = Res.drawable.icon_leaderboard_24px,
                                    text = "Статистика",
                                )
                            },
                            onClick = {
                                menuExpanded.value = false
                                onOpenStats()
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                MenuItemContent(
                                    iconRes = Res.drawable.icon_file_export_24px,
                                    text = "Экспорт в PDF",
                                )
                            },
                            onClick = {
                                menuExpanded.value = false
                                onExportAll()
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                MenuItemContent(
                                    iconRes = Res.drawable.icon_logout_24px,
                                    text = "Выйти из аккаунта",
                                )
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
            modifier = Modifier.padding(top = 32.dp),
            windowInsets = TopAppBarDefaults.windowInsets,
            scrollBehavior = scrollBehavior,
        )

        AnimatedVisibility(
            visible = searchVisible.value,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            EntriesSearchBar(
                query = searchFilter.query,
                onQueryChange = onSearchQueryChange,
                onClearQuery = onClearSearch,
                onCloseSearch = {
                    onClearSearch()
                    searchVisible.value = false
                },
            )
        }
    }
}

@Composable
private fun EntriesSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onCloseSearch: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Поиск по заметкам",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_search_24),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_cancel_24px),
                        contentDescription = "Очистить поиск",
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onClearQuery)
                            .pointerHoverIcon(PointerIcon.Hand),
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                .clickable(onClick = onCloseSearch)
                .pointerHoverIcon(PointerIcon.Hand),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_arrow_drop_up_24px),
                contentDescription = "Свернуть поиск",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntriesDateRangeFilterDialog(
    currentFilter: EntriesDateRangeFilter,
    onDismiss: () -> Unit,
    onApply: (EntriesDateRangeFilter) -> Unit,
    onClear: () -> Unit,
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = currentFilter.from?.toDatePickerMillis(),
        initialSelectedEndDateMillis = currentFilter.to?.toDatePickerMillis(),
    )
    val selectedFilter = EntriesDateRangeFilter(
        from = pickerState.selectedStartDateMillis?.toDatePickerLocalDate(),
        to = pickerState.selectedEndDateMillis?.toDatePickerLocalDate(),
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (currentFilter.isActive || selectedFilter.isActive) {
                    TextButton(
                        onClick = onClear,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    ) {
                        Text("Сбросить")
                    }
                }
                TextButton(
                    onClick = onDismiss,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                ) {
                    Text("Отмена")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onApply(selectedFilter) },
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(40.dp)
                        .widthIn(min = 140.dp),
                ) {
                    Text(
                        text = "Применить",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        softWrap = false,
                    )
                }
            }
        },
        dismissButton = {},
    ) {
        Column {
            DateRangePicker(
                state = pickerState,
                modifier = Modifier.padding(horizontal = 8.dp),
                title = {
                    Text(
                        text = "Фильтр по датам",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp),
                    )
                },
                headline = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        FilterDateSummaryCard(
                            label = "От",
                            date = selectedFilter.from,
                            modifier = Modifier.weight(1f),
                        )
                        FilterDateSummaryCard(
                            label = "До",
                            date = selectedFilter.to,
                            modifier = Modifier.weight(1f),
                        )
                    }
                },
                showModeToggle = false,
            )

        }
    }
}

@Composable
private fun FilterDateSummaryCard(
    label: String,
    date: LocalDate?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = date?.let(::formatDate) ?: "Не выбрано",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = if (date != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
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
    onExportEntry: (Entry) -> Unit,
    onClearDateFilter: () -> Unit,
    onClearSearch: () -> Unit,
) {
    val listState = remember { LazyListState() }

    val listChangeKey = when (viewState) {
        is EntriesViewState.Content -> listOf(
            viewState.entries.firstOrNull()?.id,
            viewState.entries.size.toString(),
            viewState.searchFilter.query,
            viewState.dateFilter.from?.toString(),
            viewState.dateFilter.to?.toString(),
        )
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
            is EntriesViewState.Empty -> EntriesEmptyState(
                paddingValues = paddingValues,
                dateFilter = viewState.dateFilter,
                searchFilter = viewState.searchFilter,
                onClearDateFilter = onClearDateFilter,
                onClearSearch = onClearSearch,
            )
            is EntriesViewState.Content -> EntriesListCompact(
                entries = viewState.entries,
                maxWidth = maxWidth,
                paddingValues = paddingValues,
                scrollBehavior = scrollBehavior,
                onDeleteEntry = onDeleteEntry,
                onEditEntry = onEditEntry,
                onExportEntry = onExportEntry,
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
    onExportEntry: (Entry) -> Unit,
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
                .widthIn(max = maxWidth),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(entries, key = { it.id }) { entry ->
                EntryCard(
                    entry = entry,
                    onDeleteEntry = { onDeleteEntry(entry.id) },
                    onEditEntry = { onEditEntry(entry.id) },
                    onExportEntry = { onExportEntry(entry) },
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
    onExportEntry: () -> Unit,
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
                .padding(top = if (entry.mood == null) 12.dp else 0.dp, bottom = 8.dp),
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
                            .clip(CircleShape)
                            .clickable { menuExpanded.value = true }
                            .pointerHoverIcon(PointerIcon.Hand)
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
                                    iconRes = Res.drawable.icon_export_24,
                                    text = "Экспорт в PDF",
                                )
                            },
                            onClick = {
                                menuExpanded.value = false
                                onExportEntry()
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

private fun EntriesViewState.dateFilter(): EntriesDateRangeFilter = when (this) {
    EntriesViewState.Loading -> EntriesDateRangeFilter()
    is EntriesViewState.Empty -> dateFilter
    is EntriesViewState.Content -> dateFilter
}

private fun EntriesViewState.searchFilter(): EntriesSearchFilter = when (this) {
    EntriesViewState.Loading -> EntriesSearchFilter()
    is EntriesViewState.Empty -> searchFilter
    is EntriesViewState.Content -> searchFilter
}

private fun EntriesDateRangeFilter.toDisplayText(): String = when {
    from != null && to != null -> "${formatDate(from)} - ${formatDate(to)}"
    from != null -> "От ${formatDate(from)}"
    to != null -> "До ${formatDate(to)}"
    else -> "Все даты"
}

private fun formatDateRangeHeadline(filter: EntriesDateRangeFilter): String =
    if (filter.isActive) filter.toDisplayText() else "Выбери диапазон дат"

private fun LocalDate.toDatePickerMillis(): Long =
    atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

private fun Long.toDatePickerLocalDate(): LocalDate =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC).date

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
    dateFilter: EntriesDateRangeFilter,
    searchFilter: EntriesSearchFilter,
    onClearDateFilter: () -> Unit,
    onClearSearch: () -> Unit,
) {
    val title = when {
        searchFilter.isActive -> "Ничего не найдено"
        dateFilter.isActive -> "Нет записей за выбранный период"
        else -> "Еще нет записей"
    }
    val description = if (searchFilter.isActive || dateFilter.isActive) {
        null
    } else {
        "Сделай свою первую запись.\nНажми на кнопку плюс внизу."
    }

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
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        description?.let {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        if (searchFilter.isActive || dateFilter.isActive) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (searchFilter.isActive) {
                    TextButton(onClick = onClearSearch) {
                        Text("Сбросить поиск")
                    }
                }
                if (dateFilter.isActive) {
                    TextButton(onClick = onClearDateFilter) {
                        Text("Сбросить фильтр")
                    }
                }
            }
        }
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
    val isDarkTheme = LocalJournalThemeController.current.isDarkTheme
    val colors = if (isDarkTheme) {
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


