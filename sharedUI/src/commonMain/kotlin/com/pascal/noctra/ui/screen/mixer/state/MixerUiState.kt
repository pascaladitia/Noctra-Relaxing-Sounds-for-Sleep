package com.pascal.noctra.ui.screen.mixer.state

import com.pascal.noctra.domain.model.mixer.MixerState

data class MixerUiState(
    val isLoading: Boolean = false,
    val error: Pair<Boolean, String> = false to "",
    val mixerState: MixerState = MixerState()
)
