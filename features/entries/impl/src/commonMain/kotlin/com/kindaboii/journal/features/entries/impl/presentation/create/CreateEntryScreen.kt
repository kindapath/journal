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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
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
                )
                LayoutType.Compact -> CreateEntryCompactScreen(
                    viewState = viewState,
                    onBack = onBack,
                    onDone = { viewModel.onDone(onDone) },
                    onTitleChange = viewModel::onTitleChange,
                    onBodyChange = viewModel::onBodyChange,
                    onMoodChange = viewModel::onMoodChange,
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
) {
    ConstrainedContainer(maxWidth = 900.dp) {
        CreateEntryScaffold(
            viewState = viewState,
            onBack = onBack,
            onDone = onDone,
            onTitleChange = onTitleChange,
            onBodyChange = onBodyChange,
            onMoodChange = onMoodChange,
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
) {
    CreateEntryScaffold(
        viewState = viewState,
        onBack = onBack,
        onDone = onDone,
        onTitleChange = onTitleChange,
        onBodyChange = onBodyChange,
        onMoodChange = onMoodChange,
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
            onTitleChange = onTitleChange,
            onBodyChange = onBodyChange,
            onMoodChange = onMoodChange,
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
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onMoodChange: (Mood) -> Unit,
) {
    val time = Clock.System.now()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
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
        MoodHeaderBar(
            mood = mood.value,
            emotions = mood.emotions,
            influences = mood.influences,
            time = time,
        )
        Slider(
            value = mood.value.toFloat(),
            onValueChange = { onMoodChange(mood.copy(value = it.toLong())) },
            valueRange = 1f..100f,
            modifier = Modifier.padding(top = 8.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
