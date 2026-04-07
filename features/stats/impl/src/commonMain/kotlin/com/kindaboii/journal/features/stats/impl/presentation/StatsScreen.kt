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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import journal.features.stats.impl.generated.resources.Res
import journal.features.stats.impl.generated.resources.icon_arrow_back_24
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
                    StatsScaffold(viewState = viewState, onBack = onBack)
                }
                LayoutType.Compact -> StatsScaffold(viewState = viewState, onBack = onBack)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatsScaffold(
    viewState: StatsViewState,
    onBack: () -> Unit,
) {
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
                    viewState = viewState as StatsViewState.Content,
                    paddingValues = paddingValues,
                )
            }
        }
    }
}

@Composable
private fun StatsContent(viewState: StatsViewState.Content, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SummaryCards(
            totalEntries = viewState.totalEntries,
            totalWords = viewState.totalWords,
            currentStreak = viewState.currentStreak,
        )
        MoodChartCard(moodPoints = viewState.moodPoints)
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
private fun MoodChartCard(moodPoints: List<MoodPoint>) {
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
                text = "Настроение за 30 дней",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
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
                val today = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                val startDate = today.minus(DatePeriod(days = 29))
                MoodLineChart(
                    points = moodPoints,
                    startDate = startDate,
                    endDate = today,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                )
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
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(modifier = modifier) {
        val totalDays = 29f
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
