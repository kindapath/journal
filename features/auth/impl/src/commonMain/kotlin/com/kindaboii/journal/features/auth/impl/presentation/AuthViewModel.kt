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
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(
                password = value,
                errorMessage = null,
            )
        }
    }

    fun onModeChange(mode: AuthMode) {
        _uiState.update { state ->
            state.copy(
                mode = mode,
                errorMessage = null,
            )
        }
    }

    fun submit() {
        val current = _uiState.value
        if (current.email.isBlank() || current.password.isBlank()) {
            _uiState.update { state ->
                state.copy(errorMessage = "Email and password are required")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isSubmitting = true,
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
                        state.copy(
                            isSubmitting = false,
                            password = "",
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { state ->
                        state.copy(
                            isSubmitting = false,
                            errorMessage = mapAuthError(throwable.message),
                        )
                    }
                }
        }
    }

    private fun mapAuthError(rawMessage: String?): String {
        val message = rawMessage.orEmpty()

        if (message.contains("email_not_confirmed", ignoreCase = true) ||
            message.contains("email not confirmed", ignoreCase = true)
        ) {
            return "Check your inbox and confirm your email before signing in."
        }

        if (message.contains("over_email_send_rate_limit", ignoreCase = true) ||
            message.contains("security purposes", ignoreCase = true)
        ) {
            val waitSeconds = Regex("after\\s+(\\d+)\\s+seconds", RegexOption.IGNORE_CASE)
                .find(message)
                ?.groupValues
                ?.getOrNull(1)

            return if (waitSeconds != null) {
                "Too many requests. Please wait $waitSeconds seconds and try again."
            } else {
                "Too many requests. Please wait a moment and try again."
            }
        }

        if (message.contains("invalid login credentials", ignoreCase = true)) {
            return "Invalid email or password."
        }

        if (message.contains("user already registered", ignoreCase = true)) {
            return "This email is already registered. Try signing in."
        }

        return "Authentication failed. Please try again."
    }
}
