package com.pascal.noctra.ui.screen.mixer.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalMixerEvent = compositionLocalOf { MixerEvent() }

@Stable
data class MixerEvent(
    val onNavigateBack: () -> Unit = {},
    val onNavigateToHome: () -> Unit = {}
)
