package com.kindaboii.journal.features.profile.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kindaboii.journal.domain.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authService: AuthService,
) : ViewModel() {
    private val _viewState = MutableStateFlow(
        ProfileViewState(
            currentEmail = authService.currentUserEmail().orEmpty(),
            isDemoMode = authService.currentUserEmail().isNullOrBlank(),
        )
    )
    val viewState: StateFlow<ProfileViewState> = _viewState.asStateFlow()

    fun onEmailChange(value: String) {
        _viewState.update { state ->
            state.copy(
                email = value.trim(),
                emailMessage = null,
            )
        }
    }

    fun onEmailCodeChange(value: String) {
        _viewState.update { state ->
            state.copy(
                emailCode = value.trim(),
                emailMessage = null,
            )
        }
    }

    fun onNewPasswordChange(value: String) {
        _viewState.update { state ->
            state.copy(
                newPassword = value,
                passwordMessage = null,
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _viewState.update { state ->
            state.copy(
                confirmPassword = value,
                passwordMessage = null,
            )
        }
    }

    fun continueWithRealAccount() {
        viewModelScope.launch {
            authService.signOut()
        }
    }

    fun submitEmailChange() {
        if (_viewState.value.isDemoMode) {
            continueWithRealAccount()
            return
        }

        val requestedEmail = _viewState.value.email.trim()
        val currentEmail = _viewState.value.currentEmail.trim()

        when {
            requestedEmail.isBlank() -> {
                _viewState.update { it.copy(emailMessage = "Новый email не должен быть пустым.") }
                return
            }

            requestedEmail == currentEmail -> {
                _viewState.update { it.copy(emailMessage = "Новый email должен отличаться от текущего.") }
                return
            }
        }

        viewModelScope.launch {
            _viewState.update { it.copy(isUpdatingEmail = true, emailMessage = null) }

            authService.changeEmail(requestedEmail)
                .onSuccess { result ->
                    val pendingEmail = result.pendingEmail ?: requestedEmail
                    val actualEmail = authService.currentUserEmail().orEmpty().ifBlank { result.currentEmail.orEmpty() }
                    _viewState.update {
                        it.copy(
                            isUpdatingEmail = false,
                            currentEmail = actualEmail.ifBlank { currentEmail },
                            pendingEmail = pendingEmail,
                            email = "",
                            emailCode = "",
                            emailMessage = "Код подтверждения отправлен на $pendingEmail. Введите его ниже, чтобы завершить смену email.",
                        )
                    }
                }
                .onFailure { error ->
                    _viewState.update {
                        it.copy(
                            isUpdatingEmail = false,
                            emailMessage = mapEmailError(error.message),
                        )
                    }
                }
        }
    }

    fun confirmEmailChange() {
        if (_viewState.value.isDemoMode) {
            continueWithRealAccount()
            return
        }

        val pendingEmail = _viewState.value.pendingEmail.trim()
        val code = _viewState.value.emailCode.trim()

        when {
            pendingEmail.isBlank() -> {
                _viewState.update { it.copy(emailMessage = "Сначала запросите смену email.") }
                return
            }

            code.isBlank() -> {
                _viewState.update { it.copy(emailMessage = "Введите код подтверждения из письма.") }
                return
            }
        }

        viewModelScope.launch {
            _viewState.update { it.copy(isConfirmingEmail = true, emailMessage = null) }

            authService.verifyEmailChange(
                email = pendingEmail,
                code = code,
            ).onSuccess { verifiedEmail ->
                val actualEmail = verifiedEmail.orEmpty()
                    .ifBlank { authService.currentUserEmail().orEmpty() }
                    .ifBlank { pendingEmail }
                _viewState.update {
                    it.copy(
                        isConfirmingEmail = false,
                        currentEmail = actualEmail,
                        isDemoMode = actualEmail.isBlank(),
                        pendingEmail = "",
                        emailCode = "",
                        emailMessage = "Email подтвержден и обновлен.",
                    )
                }
            }.onFailure { error ->
                _viewState.update {
                    it.copy(
                        isConfirmingEmail = false,
                        emailMessage = mapEmailVerificationError(error.message),
                    )
                }
            }
        }
    }

    fun submitPasswordChange() {
        if (_viewState.value.isDemoMode) {
            continueWithRealAccount()
            return
        }

        val password = _viewState.value.newPassword
        val confirmPassword = _viewState.value.confirmPassword

        when {
            password.isBlank() -> {
                _viewState.update { it.copy(passwordMessage = "Новый пароль не должен быть пустым.") }
                return
            }

            password.length < 6 -> {
                _viewState.update { it.copy(passwordMessage = "Новый пароль должен быть не меньше 6 символов.") }
                return
            }

            password != confirmPassword -> {
                _viewState.update { it.copy(passwordMessage = "Пароли не совпадают.") }
                return
            }
        }

        viewModelScope.launch {
            _viewState.update { it.copy(isUpdatingPassword = true, passwordMessage = null) }

            authService.changePassword(password)
                .onSuccess {
                    _viewState.update {
                        it.copy(
                            isUpdatingPassword = false,
                            newPassword = "",
                            confirmPassword = "",
                            passwordMessage = "Пароль обновлен.",
                        )
                    }
                }
                .onFailure { error ->
                    _viewState.update {
                        it.copy(
                            isUpdatingPassword = false,
                            passwordMessage = mapPasswordError(error.message),
                        )
                    }
                }
        }
    }

    private fun mapEmailError(rawMessage: String?): String {
        val message = rawMessage.orEmpty()
        return when {
            message.contains("email", ignoreCase = true) && message.contains("invalid", ignoreCase = true) ->
                "Некорректный email."
            else -> "Не удалось начать смену email. Попробуйте еще раз."
        }
    }

    private fun mapEmailVerificationError(rawMessage: String?): String {
        val message = rawMessage.orEmpty()
        return when {
            message.contains("token", ignoreCase = true) || message.contains("otp", ignoreCase = true) ->
                "Неверный или просроченный код подтверждения."
            else -> "Не удалось подтвердить смену email. Попробуйте еще раз."
        }
    }

    private fun mapPasswordError(rawMessage: String?): String {
        val message = rawMessage.orEmpty()
        return when {
            message.contains("weak", ignoreCase = true) || message.contains("password", ignoreCase = true) ->
                "Слишком слабый пароль. Он должен быть надежнее."
            else -> "Не удалось обновить пароль. Попробуйте еще раз."
        }
    }
}
