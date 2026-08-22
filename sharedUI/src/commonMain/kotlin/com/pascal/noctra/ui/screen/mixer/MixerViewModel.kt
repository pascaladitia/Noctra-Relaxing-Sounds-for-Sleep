package com.pascal.noctra.ui.screen.mixer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.noctra.data.audio.AudioPlayerManager
import com.pascal.noctra.ui.screen.mixer.state.MixerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MixerViewModel(
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MixerUiState())
    val uiState: StateFlow<MixerUiState> = _uiState.asStateFlow()

    init {
        observeMixerState()
    }

    private fun observeMixerState() {
        viewModelScope.launch {
            audioPlayerManager.mixerState.collect { mixer ->
                _uiState.update { it.copy(mixerState = mixer) }
            }
        }
    }

    fun togglePlayPause() = audioPlayerManager.togglePlayPause()
    fun updateSoundVolume(soundId: String, volume: Float) = audioPlayerManager.updateSoundVolume(soundId, volume)
    fun toggleMute(soundId: String) = audioPlayerManager.toggleMute(soundId)
    fun removeSound(soundId: String) = audioPlayerManager.removeSound(soundId)
    fun setMasterVolume(volume: Float) = audioPlayerManager.setMasterVolume(volume)
    fun startSleepTimer(durationMs: Long) = audioPlayerManager.startSleepTimer(durationMs)
    fun cancelSleepTimer() = audioPlayerManager.cancelSleepTimer()
}
