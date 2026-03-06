package com.kindaboii.journal.features.profile.impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.common.ui.ConstrainedContainer
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
) {
    val viewModel: ProfileViewModel = koinInject()
    val viewState by viewModel.viewState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Назад")
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(profileBackgroundBrush())
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter,
        ) {
            ConstrainedContainer(maxWidth = 900.dp) {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 560.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Управление аккаунтом",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        OutlinedTextField(
                            value = viewState.currentEmail,
                            onValueChange = {},
                            label = { Text("Текущий email") },
                            singleLine = true,
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        OutlinedTextField(
                            value = viewState.email,
                            onValueChange = viewModel::onEmailChange,
                            label = { Text("Новый email") },
                            singleLine = true,
                            enabled = !viewState.isUpdatingEmail && !viewState.isConfirmingEmail,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Button(
                            onClick = viewModel::submitEmailChange,
                            enabled = !viewState.isUpdatingEmail && !viewState.isConfirmingEmail,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Отправить код")
                        }

                        if (viewState.pendingEmail.isNotBlank()) {
                            Text(
                                text = "Подтверждение ожидается для ${viewState.pendingEmail}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )

                            OutlinedTextField(
                                value = viewState.emailCode,
                                onValueChange = viewModel::onEmailCodeChange,
                                label = { Text("Код подтверждения") },
                                singleLine = true,
                                enabled = !viewState.isConfirmingEmail && !viewState.isUpdatingEmail,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Button(
                                onClick = viewModel::confirmEmailChange,
                                enabled = !viewState.isConfirmingEmail && !viewState.isUpdatingEmail,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Подтвердить email")
                            }
                        }

                        viewState.emailMessage?.let { message ->
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        Text(
                            text = "Пароль",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        OutlinedTextField(
                            value = viewState.newPassword,
                            onValueChange = viewModel::onNewPasswordChange,
                            label = { Text("Новый пароль") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            enabled = !viewState.isUpdatingPassword,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        OutlinedTextField(
                            value = viewState.confirmPassword,
                            onValueChange = viewModel::onConfirmPasswordChange,
                            label = { Text("Подтвердите пароль") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            enabled = !viewState.isUpdatingPassword,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        viewState.passwordMessage?.let { message ->
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        Button(
                            onClick = viewModel::submitPasswordChange,
                            enabled = !viewState.isUpdatingPassword,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Изменить пароль")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun profileBackgroundBrush(): Brush =
    Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant,
        ),
    )