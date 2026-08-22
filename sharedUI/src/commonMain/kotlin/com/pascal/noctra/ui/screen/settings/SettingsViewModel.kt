package com.pascal.noctra.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.noctra.domain.usecase.local.settings.SettingsUseCase
import com.pascal.noctra.ui.screen.settings.state.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsUseCase: SettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsUseCase.getBackgroundPlaybackEnabled().collect { enabled ->
                _uiState.update { it.copy(backgroundPlayback = enabled) }
            }
        }
        viewModelScope.launch {
            settingsUseCase.getSoundQuality().collect { quality ->
                _uiState.update { it.copy(soundQuality = quality) }
            }
        }
        viewModelScope.launch {
            settingsUseCase.getMasterVolume().collect { volume ->
                _uiState.update { it.copy(masterVolume = volume) }
            }
        }
    }

    fun setBackgroundPlayback(enabled: Boolean) = settingsUseCase.setBackgroundPlaybackEnabled(enabled)
    fun setSoundQuality(quality: String) = settingsUseCase.setSoundQuality(quality)
    fun setMasterVolume(volume: Float) = settingsUseCase.setMasterVolume(volume)
}
