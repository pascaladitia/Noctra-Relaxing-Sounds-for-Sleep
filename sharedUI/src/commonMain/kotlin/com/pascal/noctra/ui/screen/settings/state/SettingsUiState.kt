package com.pascal.noctra.ui.screen.settings.state

data class SettingsUiState(
    val isLoading: Boolean = false,
    val error: Pair<Boolean, String> = false to "",
    val backgroundPlayback: Boolean = true,
    val soundQuality: String = "high",
    val masterVolume: Float = 0.8f
)
