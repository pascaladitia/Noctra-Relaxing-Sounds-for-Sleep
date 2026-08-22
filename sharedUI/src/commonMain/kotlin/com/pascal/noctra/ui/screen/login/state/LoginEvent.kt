package com.pascal.noctra.ui.screen.login.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalLoginEvent = compositionLocalOf { LoginEvent() }

@Stable
data class LoginEvent(
    val onEmailChanged: (String) -> Unit = {},
    val onPasswordChanged: (String) -> Unit = {},
    val onTogglePassword: () -> Unit = {},
    val onToggleMode: () -> Unit = {},
    val onEmailLogin: () -> Unit = {}
)
