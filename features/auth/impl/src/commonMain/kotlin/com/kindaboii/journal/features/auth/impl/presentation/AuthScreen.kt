package com.kindaboii.journal.features.auth.impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.common.ui.ConstrainedContainer
import com.kindaboii.journal.common.ui.LayoutType
import com.kindaboii.journal.common.ui.withLayoutType
import org.koin.compose.koinInject

@Composable
fun AuthScreen() {
    val viewModel: AuthViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(authBackgroundBrush()),
    ) {
        withLayoutType { layoutType ->
            when (layoutType) {
                LayoutType.Expanded -> AuthExpandedScreen(
                    uiState = uiState,
                    onModeChange = viewModel::onModeChange,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onConfirmationCodeChange = viewModel::onConfirmationCodeChange,
                    onSubmit = viewModel::submit,
                    onConfirmSignUp = viewModel::confirmSignUp,
                    onResendSignUpCode = viewModel::resendSignUpCode,
                )

                LayoutType.Compact -> AuthCompactScreen(
                    uiState = uiState,
                    onModeChange = viewModel::onModeChange,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onConfirmationCodeChange = viewModel::onConfirmationCodeChange,
                    onSubmit = viewModel::submit,
                    onConfirmSignUp = viewModel::confirmSignUp,
                    onResendSignUpCode = viewModel::resendSignUpCode,
                )
            }
        }
    }
}

@Composable
private fun AuthExpandedScreen(
    uiState: AuthViewState,
    onModeChange: (AuthMode) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationCodeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onConfirmSignUp: () -> Unit,
    onResendSignUpCode: () -> Unit,
) {
    ConstrainedContainer(maxWidth = 900.dp) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            AuthCard(
                uiState = uiState,
                onModeChange = onModeChange,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onConfirmationCodeChange = onConfirmationCodeChange,
                onSubmit = onSubmit,
                onConfirmSignUp = onConfirmSignUp,
                onResendSignUpCode = onResendSignUpCode,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .widthIn(max = 560.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun AuthCompactScreen(
    uiState: AuthViewState,
    onModeChange: (AuthMode) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationCodeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onConfirmSignUp: () -> Unit,
    onResendSignUpCode: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AuthCard(
            uiState = uiState,
            onModeChange = onModeChange,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onConfirmationCodeChange = onConfirmationCodeChange,
            onSubmit = onSubmit,
            onConfirmSignUp = onConfirmSignUp,
            onResendSignUpCode = onResendSignUpCode,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(max = 440.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun AuthCard(
    uiState: AuthViewState,
    onModeChange: (AuthMode) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationCodeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onConfirmSignUp: () -> Unit,
    onResendSignUpCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val awaitingConfirmation = uiState.mode == AuthMode.SignUp && uiState.pendingConfirmationEmail.isNotBlank()
    val isBusy = uiState.isSubmitting || uiState.isConfirming || uiState.isResendingCode

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Дневник",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            AuthModeSelector(
                selectedMode = uiState.mode,
                onModeChange = onModeChange,
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                enabled = !isBusy && !awaitingConfirmation,
                singleLine = true,
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isBusy && !awaitingConfirmation,
                singleLine = true,
            )

            if (awaitingConfirmation) {
                Text(
                    text = "Мы отправили код подтверждения на ${uiState.pendingConfirmationEmail}.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                OutlinedTextField(
                    value = uiState.confirmationCode,
                    onValueChange = onConfirmationCodeChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Код подтверждения") },
                    enabled = !isBusy,
                    singleLine = true,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = onConfirmSignUp,
                        enabled = !isBusy,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Подтвердить")
                    }

                    TextButton(
                        onClick = onResendSignUpCode,
                        enabled = !isBusy,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Отправить снова")
                    }
                }
            } else {
                Button(
                    onClick = onSubmit,
                    enabled = !isBusy,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (uiState.mode == AuthMode.SignIn) "Войти" else "Зарегистрироваться",
                    )
                }
            }

            uiState.infoMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun AuthModeSelector(
    selectedMode: AuthMode,
    onModeChange: (AuthMode) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AuthModeButton(
            text = "Войти",
            selected = selectedMode == AuthMode.SignIn,
            onClick = { onModeChange(AuthMode.SignIn) },
            modifier = Modifier.weight(1f),
        )
        AuthModeButton(
            text = "Зарегистрироваться",
            selected = selectedMode == AuthMode.SignUp,
            onClick = { onModeChange(AuthMode.SignUp) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AuthModeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(
                text = text,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
        }
        return
    }

    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(text)
    }
}

@Composable
private fun authBackgroundBrush(): Brush =
    Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant,
        ),
    )