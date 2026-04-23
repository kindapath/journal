package com.kindaboii.journal.features.entries.impl.presentation.unlock

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.common.ui.LayoutType
import com.kindaboii.journal.common.ui.PasswordOutlinedTextField
import com.kindaboii.journal.common.ui.withLayoutType
import org.koin.compose.koinInject

@Composable
fun EntryEncryptionUnlockScreen(
    onSignOut: () -> Unit,
) {
    val viewModel: EntryEncryptionUnlockViewModel = koinInject()
    val viewState by viewModel.viewState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(codePhraseBackgroundBrush()),
        contentAlignment = Alignment.Center,
    ) {
        withLayoutType { layoutType ->
            val cardMaxWidth = when (layoutType) {
                LayoutType.Expanded -> 560.dp
                LayoutType.Compact -> 440.dp
            }

            EntryEncryptionUnlockCard(
                viewState = viewState,
                onPassphraseChange = viewModel::onPassphraseChange,
                onRepeatedPassphraseChange = viewModel::onRepeatedPassphraseChange,
                onDataLossRiskAcceptedChange = viewModel::onDataLossRiskAcceptedChange,
                onRememberOnThisDeviceChange = viewModel::onRememberOnThisDeviceChange,
                onSubmit = viewModel::submit,
                onSignOut = onSignOut,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .widthIn(max = cardMaxWidth)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun EntryEncryptionUnlockCard(
    viewState: EntryEncryptionUnlockViewState,
    onPassphraseChange: (String) -> Unit,
    onRepeatedPassphraseChange: (String) -> Unit,
    onDataLossRiskAcceptedChange: (Boolean) -> Unit,
    onRememberOnThisDeviceChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            when {
                viewState.isLoading -> LoadingContent()
                else -> CodePhraseContent(
                    viewState = viewState,
                    onPassphraseChange = onPassphraseChange,
                    onRepeatedPassphraseChange = onRepeatedPassphraseChange,
                    onDataLossRiskAcceptedChange = onDataLossRiskAcceptedChange,
                    onRememberOnThisDeviceChange = onRememberOnThisDeviceChange,
                    onSubmit = onSubmit,
                    onSignOut = onSignOut,
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(
            text = "Готовим защищённый дневник...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun CodePhraseContent(
    viewState: EntryEncryptionUnlockViewState,
    onPassphraseChange: (String) -> Unit,
    onRepeatedPassphraseChange: (String) -> Unit,
    onDataLossRiskAcceptedChange: (Boolean) -> Unit,
    onRememberOnThisDeviceChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onSignOut: () -> Unit,
) {
    val mode = viewState.mode ?: return
    val isCreateMode = mode == EntryEncryptionUnlockMode.Create
    val title = if (isCreateMode) {
        "Создайте кодовую фразу"
    } else {
        "Введите кодовую фразу"
    }
    val description = if (isCreateMode) {
        "Она будет защищать ваши записи перед синхронизацией. Без этой фразы восстановить зашифрованные данные не получится."
    } else {
        "Введите кодовую фразу, которую вы задавали для этого дневника. Она нужна, чтобы открыть записи после синхронизации."
    }
    val buttonText = if (isCreateMode) {
        "Создать и открыть дневник"
    } else {
        "Открыть дневник"
    }
    val canSubmit = !viewState.isSubmitting &&
            (!isCreateMode || viewState.dataLossRiskAccepted)

    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
    )

    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (isCreateMode) {
        Text(
            text = "Сохраните кодовую фразу в надёжном месте. Мы не храним её и не сможем восстановить доступ без неё.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
        )
    }

    PasswordOutlinedTextField(
        value = viewState.passphrase,
        onValueChange = onPassphraseChange,
        modifier = Modifier.fillMaxWidth(),
        label = "Кодовая фраза",
        enabled = !viewState.isSubmitting,
    )

    if (isCreateMode) {
        PasswordOutlinedTextField(
            value = viewState.repeatedPassphrase,
            onValueChange = onRepeatedPassphraseChange,
            modifier = Modifier.fillMaxWidth(),
            label = "Повторите кодовую фразу",
            enabled = !viewState.isSubmitting,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = viewState.dataLossRiskAccepted,
                onCheckedChange = onDataLossRiskAcceptedChange,
                enabled = !viewState.isSubmitting,
            )

            Text(
                text = "Я понимаю, что без кодовой фразы доступ к зашифрованным данным восстановить не получится.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

    if (viewState.canRememberOnThisDevice) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = viewState.rememberOnThisDevice,
                onCheckedChange = onRememberOnThisDeviceChange,
                enabled = !viewState.isSubmitting,
            )

            Text(
                text = "Запомнить на этом устройстве",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Text(
            text = "Кодовая фраза будет сохранена только на этом устройстве или браузере.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    viewState.errorMessage?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
        )
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSubmit,
        enabled = canSubmit,
    ) {
        if (viewState.isSubmitting) {
            CircularProgressIndicator(
                modifier = Modifier.height(18.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Text(buttonText)
        }
    }

    Spacer(modifier = Modifier.height(2.dp))

    TextButton(
        onClick = onSignOut,
        enabled = !viewState.isSubmitting,
    ) {
        Text("Выйти из аккаунта")
    }
}

@Composable
private fun codePhraseBackgroundBrush(): Brush =
    Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant,
        ),
    )
