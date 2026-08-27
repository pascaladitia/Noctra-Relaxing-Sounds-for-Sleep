package com.pascal.noctra.ui.screen.home.state

import com.pascal.noctra.domain.model.preset.Preset
import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: Pair<Boolean, String> = false to "",
    val sounds: List<Sound> = emptyList(),
    val presets: List<Preset> = emptyList(),
    val activeSoundIds: Set<String> = emptySet(),
    val selectedCategory: SoundCategory = SoundCategory.ALL
)
