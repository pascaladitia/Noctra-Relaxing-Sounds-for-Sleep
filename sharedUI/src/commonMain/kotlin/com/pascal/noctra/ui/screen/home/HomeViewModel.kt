package com.pascal.noctra.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.noctra.data.audio.AudioPlayerManager
import com.pascal.noctra.domain.model.preset.Preset
import com.pascal.noctra.domain.model.sound.ActiveSound
import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory
import com.pascal.noctra.domain.usecase.local.preset.PresetUseCase
import com.pascal.noctra.domain.usecase.local.sound.SoundUseCase
import com.pascal.noctra.ui.screen.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val soundUseCase: SoundUseCase,
    private val presetUseCase: PresetUseCase,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSounds()
        loadPresets()
        observeActiveSounds()
    }

    private fun loadSounds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            soundUseCase.getSounds()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = true to (e.message ?: "Error")) }
                }
                .collect { sounds ->
                    _uiState.update { it.copy(isLoading = false, sounds = sounds) }
                }
        }
    }

    private fun loadPresets() {
        viewModelScope.launch {
            presetUseCase.getCuratedPresets()
                .collect { presets ->
                    _uiState.update { it.copy(presets = presets) }
                }
        }
    }

    private fun observeActiveSounds() {
        viewModelScope.launch {
            audioPlayerManager.mixerState.collect { mixer ->
                _uiState.update {
                    it.copy(activeSoundIds = mixer.activeSounds.map { s -> s.sound.id }.toSet())
                }
            }
        }
    }

    fun toggleSound(sound: Sound) {
        val current = _uiState.value
        if (current.activeSoundIds.contains(sound.id)) {
            audioPlayerManager.removeSound(sound.id)
        } else {
            audioPlayerManager.addSound(ActiveSound(sound = sound))
        }
    }

    fun playPreset(preset: Preset) {
        audioPlayerManager.loadPresetSounds(preset.sounds)
    }

    fun setCategory(category: SoundCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }
}
