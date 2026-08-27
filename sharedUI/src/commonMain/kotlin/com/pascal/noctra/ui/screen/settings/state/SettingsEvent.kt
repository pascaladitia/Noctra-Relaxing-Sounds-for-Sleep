package com.pascal.noctra.ui.screen.settings.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalSettingsEvent = compositionLocalOf { SettingsEvent() }

@Stable
data class SettingsEvent(
    val onNavigateBack: () -> Unit = {}
)
