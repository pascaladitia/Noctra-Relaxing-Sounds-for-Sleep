package com.pascal.noctra.ui.screen.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import com.pascal.noctra.domain.model.preset.Preset
import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory

val LocalHomeEvent = compositionLocalOf { HomeEvent() }

@Stable
data class HomeEvent(
    val onNavigateToMixer: () -> Unit = {},
    val onNavigateToSettings: () -> Unit = {},
    val onToggleSound: (Sound) -> Unit = {},
    val onPlayPreset: (Preset) -> Unit = {},
    val onCategorySelected: (SoundCategory) -> Unit = {}
)
