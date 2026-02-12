package com.kindaboii.journal.features.entries.impl.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.common.ui.ConstrainedContainer
import com.kindaboii.journal.common.ui.LayoutType
import com.kindaboii.journal.common.ui.withLayoutType
import com.kindaboii.journal.features.entries.api.models.Mood
import com.kindaboii.journal.features.entries.impl.presentation.components.MoodHeaderBar
import com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin.CollapsedMoodBar
import com.kindaboii.journal.features.entries.impl.presentation.create.moodcheckin.MoodCheckInSheet
import journal.features.entries.impl.generated.resources.Res
import journal.features.entries.impl.generated.resources.close_24px
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock
import org.koin.compose.koinInject

@Composable
fun CreateEntryScreen(
    entryId: String?,
    onBack: () -> Unit,
    onDone: () -> Unit,
) {
    val viewModel: CreateEntryViewModel = koinInject()
    val viewState = viewModel.viewState.collectAsState().value.data

    LaunchedEffect(entryId) {
        viewModel.loadEntry(entryId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        withLayoutType { layoutType ->
            when (layoutType) {
                LayoutType.Expanded -> CreateEntryExpandedScreen(
                    viewState = viewState,
                    onBack = onBack,
                    onDone = { viewModel.onDone(onDone) },
                    onTitleChange = viewModel::onTitleChange,
                    onBodyChange = viewModel::onBodyChange,
                    onMoodChange = viewModel::onMoodChange,
                    onMoodClear = viewModel::onMoodClear,
                )
                LayoutType.Compact -> CreateEntryCompactScreen(
                    viewState = viewState,
                    onBack = onBack,
                    onDone = { viewModel.onDone(onDone) },
                    onTitleChange = viewModel::onTitleChange,
                    onBodyChange = viewModel::onBodyChange,
                    onMoodChange = viewModel::onMoodChange,
                    onMoodClear = viewModel::onMoodClear,
                )
            }
        }
    }
}

@Composable
private fun CreateEntryExpandedScreen(
    viewState: CreateEntryViewState.Data,
    onBack: () -> Unit,
    onDone: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onMoodChange: (Mood) -> Unit,
    onMoodClear: () -> Unit,
) {
    ConstrainedContainer(maxWidth = 900.dp) {
        CreateEntryScaffold(
            viewState = viewState,
            onBack = onBack,
            onDone = onDone,
            onTitleChange = onTitleChange,
            onBodyChange = onBodyChange,
            onMoodChange = onMoodChange,
            onMoodClear = onMoodClear,
        )
    }
}

@Composable
private fun CreateEntryCompactScreen(
    viewState: CreateEntryViewState.Data,
    onBack: () -> Unit,
    onDone: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onMoodChange: (Mood) -> Unit,
    onMoodClear: () -> Unit,
) {
    CreateEntryScaffold(
        viewState = viewState,
        onBack = onBack,
        onDone = onDone,
        onTitleChange = onTitleChange,
        onBodyChange = onBodyChange,
        onMoodChange = onMoodChange,
        onMoodClear = onMoodClear,
    )
}

@Composable
private fun CreateEntryScaffold(
    viewState: CreateEntryViewState.Data,
    onBack: () -> Unit,
    onDone: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onMoodChange: (Mood) -> Unit,
    onMoodClear: () -> Unit,
) {
    Scaffold(
        topBar = { CreateEntryTopBar(onBack = onBack, onDone = onDone) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) { paddingValues ->
        CreateEntryContent(
            paddingValues = paddingValues,
            title = viewState.title,
            body = viewState.body,
            mood = viewState.mood,
            hasMoodCheckIn = viewState.hasMoodCheckIn,
            onTitleChange = onTitleChange,
            onBodyChange = onBodyChange,
            onMoodChange = onMoodChange,
            onMoodClear = onMoodClear,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateEntryTopBar(
    onBack: () -> Unit,
    onDone: () -> Unit,
) {
    TopAppBar(
        title = { },
        modifier = Modifier.padding(top = 24.dp),
        windowInsets = TopAppBarDefaults.windowInsets,
        navigationIcon = {
            Text(
                text = "Назад",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = MaterialTheme.shapes.small,
                    )
                    .clickable { onBack() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
            )
        },
        actions = {
            Text(
                text = "Ещё",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
            )
            Text(
                text = "Готово",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { onDone() }
                    .pointerHoverIcon(PointerIcon.Hand),
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Composable
private fun CreateEntryContent(
    paddingValues: PaddingValues,
    title: String,
    body: String,
    mood: Mood,
    hasMoodCheckIn: Boolean,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onMoodChange: (Mood) -> Unit,
    onMoodClear: () -> Unit,
) {
    val time = Clock.System.now()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (hasMoodCheckIn) {
            Box(modifier = Modifier.fillMaxWidth()) {
                MoodHeaderBar(
                    mood = mood.value,
                    emotions = mood.emotions,
                    influences = mood.influences,
                    time = time,
                    modifier = Modifier.padding(bottom = 8.dp),
                    height = 84.dp,
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                        .size(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            shape = CircleShape,
                        )
                        .clickable { onMoodClear() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.close_24px),
                        contentDescription = "Удалить результат настроения",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }

        BasicTextField(
            value = title,
            onValueChange = onTitleChange,
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = "Заголовок",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                innerTextField()
            },
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
            thickness = 0.5.dp,
        )
        BasicTextField(
            value = body,
            onValueChange = onBodyChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (body.isEmpty()) {
                    Text(
                        text = "Начните писать...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                innerTextField()
            },
        )
        Spacer(modifier = Modifier.weight(1f))

        var showMoodSheet by remember { mutableStateOf(false) }

        CollapsedMoodBar(
            mood = mood,
            time = time,
            onClick = { showMoodSheet = true },
            modifier = Modifier.align(Alignment.End),
        )

        if (showMoodSheet) {
            MoodCheckInSheet(
                currentMood = mood,
                onDismiss = { showMoodSheet = false },
                onConfirm = { newMood ->
                    onMoodChange(newMood)
                    showMoodSheet = false
                },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
