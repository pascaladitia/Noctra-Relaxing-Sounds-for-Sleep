package com.pascal.noctra.domain.model.mixer

import com.pascal.noctra.domain.model.sound.ActiveSound
import com.pascal.noctra.domain.model.timer.SleepTimerState

data class MixerState(
    val activeSounds: List<ActiveSound> = emptyList(),
    val masterVolume: Float = 0.8f,
    val isPlaying: Boolean = false,
    val sleepTimer: SleepTimerState = SleepTimerState()
) {
    val activeSoundCount: Int get() = activeSounds.size
    val hasActiveSounds: Boolean get() = activeSounds.isNotEmpty()
}
