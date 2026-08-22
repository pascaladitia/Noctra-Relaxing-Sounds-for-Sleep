package com.pascal.noctra.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.noctra.domain.model.auth.AuthUser
import com.pascal.noctra.domain.usecase.remote.auth.AuthUseCase
import com.pascal.noctra.ui.screen.login.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, error = false to "") }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, error = false to "") }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleMode() {
        _uiState.update {
            it.copy(
                isRegisterMode = !it.isRegisterMode,
                error = false to ""
            )
        }
    }

    fun loginWithEmail(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!validate(state.email, state.password)) return

        viewModelScope.launch {
            runAuth(onSuccess) {
                if (state.isRegisterMode) {
                    authUseCase.registerWithEmail(state.email, state.password)
                } else {
                    authUseCase.loginWithEmail(state.email, state.password)
                }
            }
        }
    }

    fun hideDialog() {
        _uiState.update { it.copy(error = false to "") }
    }

    private suspend fun runAuth(
        onSuccess: () -> Unit,
        block: suspend () -> AuthUser
    ) {
        _uiState.update { it.copy(isLoading = true, error = false to "") }
        try {
            val user = block()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    userName = user.displayName ?: user.email.orEmpty(),
                    fcmToken = user.fcmToken
                )
            }
            onSuccess()
        } catch (error: Throwable) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = true to (error.message ?: "Login gagal. Coba lagi.")
                )
            }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        val message = when {
            email.isBlank() -> "Email wajib diisi."
            "@" !in email -> "Format email belum valid."
            password.length < 6 -> "Password minimal 6 karakter."
            else -> null
        }

        if (message != null) {
            _uiState.update { it.copy(error = true to message) }
            return false
        }
        return true
    }
}
