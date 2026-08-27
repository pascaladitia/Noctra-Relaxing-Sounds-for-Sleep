package com.pascal.noctra.ui.screen.login.state

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: Pair<Boolean, String> = false to "",

    val email: String = "",
    val password: String = "",

    val userName: String = "",
    val fcmToken: String? = null,

    val isRegisterMode: Boolean = false,
    val isPasswordVisible: Boolean = false
)
