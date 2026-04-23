package com.kindaboii.journal.features.profile.impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kindaboii.journal.common.ui.ConstrainedContainer
import com.kindaboii.journal.common.ui.LayoutType
import com.kindaboii.journal.common.ui.PasswordOutlinedTextField
import com.kindaboii.journal.common.ui.withLayoutType
import journal.features.profile.impl.generated.resources.Res
import journal.features.profile.impl.generated.resources.icon_arrow_back_24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
) {
    val viewModel: ProfileViewModel = koinInject()
    val viewState by viewModel.viewState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(profileBackgroundBrush()),
    ) {
        withLayoutType { layoutType ->
            when (layoutType) {
                LayoutType.Expanded -> ConstrainedContainer(maxWidth = 900.dp) {
                    ProfileScaffold(viewState = viewState, viewModel = viewModel, onBack = onBack)
                }
                LayoutType.Compact -> ProfileScaffold(viewState = viewState, viewModel = viewModel, onBack = onBack)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScaffold(
    viewState: ProfileViewState,
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    ) {
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
        ProfileContent(
            viewState = viewState,
            viewModel = viewModel,
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun ProfileContent(
    viewState: ProfileViewState,
    viewModel: ProfileViewModel,
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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

                Text(
                    text = "Изменить email",
                    style = MaterialTheme.typography.titleMedium,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerHoverIcon(PointerIcon.Hand),
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerHoverIcon(PointerIcon.Hand),
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
                    text = "Изменить пароль",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                PasswordOutlinedTextField(
                    value = viewState.newPassword,
                    onValueChange = viewModel::onNewPasswordChange,
                    label = "Новый пароль",
                    enabled = !viewState.isUpdatingPassword,
                    modifier = Modifier.fillMaxWidth(),
                )

                PasswordOutlinedTextField(
                    value = viewState.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = "Подтвердите пароль",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerHoverIcon(PointerIcon.Hand),
                ) {
                    Text("Изменить пароль")
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

