package com.kindaboii.journal.features.stats.impl.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.common.colors.JournalColors
import com.kindaboii.journal.common.ui.ConstrainedContainer
import com.kindaboii.journal.common.ui.LayoutType
import com.kindaboii.journal.common.ui.LocalJournalThemeController
import com.kindaboii.journal.common.ui.withLayoutType
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import journal.features.stats.impl.generated.resources.Res
import journal.features.stats.impl.generated.resources.icon_arrow_back_24
import journal.features.stats.impl.generated.resources.icon_date_range_24px
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun StatsScreen(
    onBack: () -> Unit,
) {
    val viewModel: StatsViewModel = koinInject()
    val viewState by viewModel.viewState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(statsBackgroundBrush()),
    ) {
        withLayoutType { layoutType ->
            when (layoutType) {
                LayoutType.Expanded -> ConstrainedContainer(maxWidth = 900.dp) {
                    StatsScaffold(
                        viewState = viewState,
                        onBack = onBack,
                        onApplyDateFilter = viewModel::onApplyDateFilter,
                        onClearDateFilter = viewModel::onClearDateFilter,
                    )
                }
                LayoutType.Compact -> StatsScaffold(
                    viewState = viewState,
                    onBack = onBack,
                    onApplyDateFilter = viewModel::onApplyDateFilter,
                    onClearDateFilter = viewModel::onClearDateFilter,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatsScaffold(
    viewState: StatsViewState,
    onBack: () -> Unit,
    onApplyDateFilter: (LocalDate?, LocalDate?) -> Unit,
    onClearDateFilter: () -> Unit,
) {
    val dateFilter = viewState.dateFilter()
    val filterDialogVisible = remember { mutableStateOf(false) }

    if (filterDialogVisible.value) {
        StatsDateRangeFilterDialog(
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

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Статистика") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_arrow_back_24),
                            contentDescription = "Назад",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
                expandedHeight = 96.dp,
            )
        },
        containerColor = Color.Transparent,
    ) { paddingValues ->
        when (viewState) {
            StatsViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is StatsViewState.Content -> {
                StatsContent(
                    viewState = viewState,
                    paddingValues = paddingValues,
                    onOpenDateFilter = { filterDialogVisible.value = true },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatsDateRangeFilterDialog(
    currentFilter: StatsDateRangeFilter,
    onDismiss: () -> Unit,
    onApply: (StatsDateRangeFilter) -> Unit,
    onClear: () -> Unit,
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = currentFilter.from?.toDatePickerMillis(),
        initialSelectedEndDateMillis = currentFilter.to?.toDatePickerMillis(),
    )
    val selectedFilter = StatsDateRangeFilter(
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
                    TextButton(onClick = onClear) {
                        Text("Сбросить")
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { onApply(selectedFilter) }) {
                    Text("Применить")
                }
            }
        },
        dismissButton = {},
    ) {
        DateRangePicker(
            state = pickerState,
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
                    StatsFilterDateSummaryCard(
                        label = "От",
                        date = selectedFilter.from,
                        modifier = Modifier.weight(1f),
                    )
                    StatsFilterDateSummaryCard(
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

@Composable
private fun StatsFilterDateSummaryCard(
    label: String,
    date: LocalDate?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                shape = RoundedCornerShape(18.dp),
            )
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

@Composable
private fun StatsContent(
    viewState: StatsViewState.Content,
    paddingValues: PaddingValues,
    onOpenDateFilter: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (viewState.dateFilter.isActive) {
            Text(
                text = "Период: ${viewState.dateFilter.toDisplayText()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        SummaryCards(
            totalEntries = viewState.totalEntries,
            totalWords = viewState.totalWords,
            currentStreak = viewState.currentStreak,
        )
        MoodChartCard(
            moodPoints = viewState.moodPoints,
            startDate = viewState.chartStartDate,
            endDate = viewState.chartEndDate,
            dateFilter = viewState.dateFilter,
            onOpenDateFilter = onOpenDateFilter,
        )
    }
}

@Composable
private fun SummaryCards(
    totalEntries: Int,
    totalWords: Int,
    currentStreak: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            label = "Записей",
            value = totalEntries.toString(),
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = "Слов",
            value = totalWords.toString(),
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = "Серия",
            value = pluralDays(currentStreak),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun MoodChartCard(
    moodPoints: List<MoodPoint>,
    startDate: LocalDate,
    endDate: LocalDate,
    dateFilter: StatsDateRangeFilter,
    onOpenDateFilter: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Text(
                text = if (dateFilter.isActive) "Настроение за период" else "Настроение за 30 дней",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${formatDate(startDate)} - ${formatDate(endDate)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (moodPoints.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Нет данных о настроении",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                MoodLineChart(
                    points = moodPoints,
                    startDate = startDate,
                    endDate = endDate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onOpenDateFilter,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_date_range_24px),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = "Выбрать период",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodLineChart(
    points: List<MoodPoint>,
    startDate: LocalDate,
    endDate: LocalDate,
    modifier: Modifier = Modifier,
) {
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    Canvas(modifier = modifier) {
        val totalDays = daysBetween(startDate, endDate).coerceAtLeast(1).toFloat()
        val chartWidth = size.width
        val chartHeight = size.height - 20.dp.toPx() // bottom space for labels

        // Draw horizontal grid lines (at 0%, 50%, 100% of mood)
        listOf(0f, 0.5f, 1f).forEach { ratio ->
            val y = chartHeight * (1f - ratio)
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1.dp.toPx(),
            )
        }

        if (points.size < 2) {
            // Draw single dot if only one point
            points.firstOrNull()?.let { point ->
                val dayIndex = daysBetween(startDate, point.date).toFloat()
                val x = (dayIndex / totalDays) * chartWidth
                val y = chartHeight * (1f - point.value / 100f)
                drawCircle(
                    color = moodColor(point.value),
                    radius = 5.dp.toPx(),
                    center = Offset(x, y),
                )
            }
            return@Canvas
        }

        // Build path
        val path = Path()
        var firstPoint = true
        points.forEach { point ->
            val dayIndex = daysBetween(startDate, point.date).toFloat()
            val x = (dayIndex / totalDays) * chartWidth
            val y = chartHeight * (1f - point.value / 100f)
            if (firstPoint) {
                path.moveTo(x, y)
                firstPoint = false
            } else {
                path.lineTo(x, y)
            }
        }

        // Draw the line
        drawPath(
            path = path,
            color = JournalColors.Primary.copy(alpha = 0.7f),
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )

        // Draw filled area under the line
        val fillPath = Path()
        fillPath.addPath(path)
        val lastPoint = points.last()
        val lastDayIndex = daysBetween(startDate, lastPoint.date).toFloat()
        val lastX = (lastDayIndex / totalDays) * chartWidth
        fillPath.lineTo(lastX, chartHeight)
        val firstDayIndex = daysBetween(startDate, points.first().date).toFloat()
        val firstX = (firstDayIndex / totalDays) * chartWidth
        fillPath.lineTo(firstX, chartHeight)
        fillPath.close()
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    JournalColors.Primary.copy(alpha = 0.18f),
                    JournalColors.Primary.copy(alpha = 0f),
                ),
                startY = 0f,
                endY = chartHeight,
            ),
        )

        // Draw data points
        points.forEach { point ->
            val dayIndex = daysBetween(startDate, point.date).toFloat()
            val x = (dayIndex / totalDays) * chartWidth
            val y = chartHeight * (1f - point.value / 100f)
            drawCircle(
                color = moodColor(point.value),
                radius = 5.dp.toPx(),
                center = Offset(x, y),
            )
            drawCircle(
                color = Color.White,
                radius = 2.5f.dp.toPx(),
                center = Offset(x, y),
            )
        }
    }
}

private fun StatsViewState.dateFilter(): StatsDateRangeFilter = when (this) {
    StatsViewState.Loading -> StatsDateRangeFilter()
    is StatsViewState.Content -> dateFilter
}

private fun StatsDateRangeFilter.toDisplayText(): String = when {
    from != null && to != null -> "${formatDate(from)} - ${formatDate(to)}"
    from != null -> "От ${formatDate(from)}"
    to != null -> "До ${formatDate(to)}"
    else -> "Все даты"
}

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

private fun daysBetween(start: LocalDate, end: LocalDate): Long =
    end.toEpochDays() - start.toEpochDays()

private fun moodColor(value: Long): Color = when {
    value < 15 -> JournalColors.MoodVeryUnpleasant
    value < 29 -> JournalColors.MoodUnpleasant
    value < 43 -> JournalColors.MoodSlightlyUnpleasant
    value < 57 -> JournalColors.MoodNeutral
    value < 71 -> JournalColors.MoodSlightlyPleasant
    value < 86 -> JournalColors.MoodPleasant
    else -> JournalColors.MoodVeryPleasant
}

private fun pluralDays(days: Int): String = when {
    days == 0 -> "0 дней"
    days % 100 in 11..19 -> "$days дней"
    days % 10 == 1 -> "$days день"
    days % 10 in 2..4 -> "$days дня"
    else -> "$days дней"
}

@Composable
private fun statsBackgroundBrush(): Brush =
    if (LocalJournalThemeController.current.isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                JournalColors.BackgroundDark,
                JournalColors.SurfaceDark,
            ),
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                JournalColors.BackgroundLight,
                JournalColors.SurfaceVariantLight,
            ),
        )
    }
