package com.kindaboii.journal.features.auth.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.domain.AuthService
import kotlin.text.Regex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authService: AuthService,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthViewState())
    val uiState: StateFlow<AuthViewState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { state ->
            state.copy(
                email = value.trim(),
                errorMessage = null,
                infoMessage = null,
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(
                password = value,
                errorMessage = null,
                infoMessage = null,
            )
        }
    }

    fun onConfirmationCodeChange(value: String) {
        _uiState.update { state ->
            state.copy(
                confirmationCode = value.trim(),
                errorMessage = null,
                infoMessage = null,
            )
        }
    }

    fun onModeChange(mode: AuthMode) {
        _uiState.update { state ->
            state.copy(
                mode = mode,
                password = "",
                confirmationCode = "",
                pendingConfirmationEmail = "",
                infoMessage = null,
                errorMessage = null,
            )
        }
    }

    fun submit() {
        val current = _uiState.value
        if (current.email.isBlank() || current.password.isBlank()) {
            _uiState.update { state ->
                state.copy(errorMessage = "Введите email и пароль.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isSubmitting = true,
                    isDemoSubmitting = false,
                    infoMessage = null,
                    errorMessage = null,
                )
            }

            val result = when (current.mode) {
                AuthMode.SignIn -> authService.signIn(
                    email = current.email,
                    password = current.password,
                )

                AuthMode.SignUp -> authService.signUp(
                    email = current.email,
                    password = current.password,
                )
            }

            result
                .onSuccess {
                    _uiState.update { state ->
                        when (current.mode) {
                            AuthMode.SignIn -> state.copy(
                                isSubmitting = false,
                                isDemoSubmitting = false,
                                password = "",
                                confirmationCode = "",
                                pendingConfirmationEmail = "",
                                infoMessage = null,
                                errorMessage = null,
                            )

                            AuthMode.SignUp -> state.copy(
                                isSubmitting = false,
                                isDemoSubmitting = false,
                                password = "",
                                pendingConfirmationEmail = current.email,
                                confirmationCode = "",
                                infoMessage = "Код подтверждения отправлен на ${current.email}. Введите его ниже, чтобы завершить регистрацию.",
                                errorMessage = null,
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isSubmitting = false,
                            isDemoSubmitting = false,
                            errorMessage = mapAuthError(throwable.message),
                        )
                    }
                }
        }
    }

    fun submitDemo() {
        if (_uiState.value.isDemoSubmitting) return

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isSubmitting = false,
                    isDemoSubmitting = true,
                    infoMessage = null,
                    errorMessage = null,
                    password = "",
                    confirmationCode = "",
                    pendingConfirmationEmail = "",
                )
            }

            authService.signInDemo()
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isDemoSubmitting = false,
                            infoMessage = null,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isDemoSubmitting = false,
                            errorMessage = mapDemoError(throwable.message),
                        )
                    }
                }
        }
    }

    fun confirmSignUp() {
        val current = _uiState.value
        val email = current.pendingConfirmationEmail.trim()
        val code = current.confirmationCode.trim()

        when {
            email.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Сначала зарегистрируйтесь, чтобы получить код подтверждения.") }
                return
            }

            code.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Введите код подтверждения из письма.") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isConfirming = true,
                    isDemoSubmitting = false,
                    infoMessage = null,
                    errorMessage = null,
                )
            }

            authService.verifySignUp(email = email, code = code)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            mode = AuthMode.SignIn,
                            email = email,
                            password = "",
                            confirmationCode = "",
                            pendingConfirmationEmail = "",
                            isConfirming = false,
                            isDemoSubmitting = false,
                            infoMessage = "Почта подтверждена. Теперь войдите в аккаунт.",
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isConfirming = false,
                            isDemoSubmitting = false,
                            errorMessage = mapConfirmationError(throwable.message),
                        )
                    }
                }
        }
    }

    fun resendSignUpCode() {
        val email = _uiState.value.pendingConfirmationEmail.trim()

        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Нет email для повторной отправки кода.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isResendingCode = true,
                    isDemoSubmitting = false,
                    infoMessage = null,
                    errorMessage = null,
                )
            }

            authService.resendSignUpConfirmation(email)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isResendingCode = false,
                            isDemoSubmitting = false,
                            infoMessage = "Новый код отправлен на $email.",
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isResendingCode = false,
                            isDemoSubmitting = false,
                            errorMessage = mapAuthError(throwable.message),
                        )
                    }
                }
        }
    }

    private fun mapDemoError(rawMessage: String?): String {
        val message = rawMessage.orEmpty().trim()

        if (message.contains("demo access is not enabled", ignoreCase = true)) {
            return "Демо-доступ отключен в этой сборке приложения."
        }

        if (message.contains("anonymous", ignoreCase = true) &&
            (message.contains("disabled", ignoreCase = true) || message.contains("enable", ignoreCase = true))
        ) {
            return "В Supabase еще не включены Anonymous Sign-Ins. Нужно включить их в Dashboard: Authentication -> Providers -> Anonymous."
        }

        if (message.contains("captcha", ignoreCase = true)) {
            return "Supabase запросил CAPTCHA для demo-входа. Нужно либо настроить CAPTCHA, либо отключить ее для anonymous sign-ins."
        }

        if (message.contains("provider", ignoreCase = true) && message.contains("disabled", ignoreCase = true)) {
            return "Похоже, провайдер anonymous login отключен в Supabase."
        }

        val compactMessage = message
            .replace('\n', ' ')
            .replace(Regex("\\s+"), " ")
            .trim()

        return if (compactMessage.isNotBlank()) {
            "Не удалось войти в demo-режим: $compactMessage"
        } else {
            "Не удалось войти в demo-режим. Проверьте, что в Supabase включены Anonymous Sign-Ins."
        }
    }

    private fun mapAuthError(rawMessage: String?): String {
        val message = rawMessage.orEmpty()

        if (message.contains("email_not_confirmed", ignoreCase = true) ||
            message.contains("email not confirmed", ignoreCase = true)
        ) {
            return "Подтвердите email перед входом."
        }

        if (message.contains("over_email_send_rate_limit", ignoreCase = true) ||
            message.contains("security purposes", ignoreCase = true)
        ) {
            val waitSeconds = Regex("after\\s+(\\d+)\\s+seconds", RegexOption.IGNORE_CASE)
                .find(message)
                ?.groupValues
                ?.getOrNull(1)

            return if (waitSeconds != null) {
                "Слишком много запросов. Подождите $waitSeconds сек. и попробуйте снова."
            } else {
                "Слишком много запросов. Подождите немного и попробуйте снова."
            }
        }

        if (message.contains("invalid login credentials", ignoreCase = true)) {
            return "Неверный email или пароль."
        }

        if (message.contains("user already registered", ignoreCase = true)) {
            return "Этот email уже зарегистрирован. Попробуйте войти."
        }

        if (message.contains("invalid", ignoreCase = true) && message.contains("email", ignoreCase = true)) {
            return "Некорректный email."
        }

        return "Не удалось выполнить авторизацию. Попробуйте еще раз."
    }

    private fun mapConfirmationError(rawMessage: String?): String {
        val message = rawMessage.orEmpty()

        if (message.contains("token", ignoreCase = true) ||
            message.contains("otp", ignoreCase = true)
        ) {
            return "Неверный или просроченный код подтверждения."
        }

        return "Не удалось подтвердить email. Попробуйте еще раз."
    }
}
