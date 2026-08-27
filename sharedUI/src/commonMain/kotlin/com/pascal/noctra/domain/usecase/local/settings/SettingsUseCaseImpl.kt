package com.pascal.noctra.domain.usecase.local.settings

import com.pascal.noctra.data.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SettingsUseCase {
    override fun getBackgroundPlaybackEnabled(): Flow<Boolean> = settingsRepository.getBackgroundPlaybackEnabled()
    override fun setBackgroundPlaybackEnabled(enabled: Boolean) = settingsRepository.setBackgroundPlaybackEnabled(enabled)
    override fun getSoundQuality(): Flow<String> = settingsRepository.getSoundQuality()
    override fun setSoundQuality(quality: String) = settingsRepository.setSoundQuality(quality)
    override fun getOnboardingCompleted(): Boolean = settingsRepository.getOnboardingCompleted()
    override fun setOnboardingCompleted(completed: Boolean) = settingsRepository.setOnboardingCompleted(completed)
    override fun getMasterVolume(): Flow<Float> = settingsRepository.getMasterVolume()
    override fun setMasterVolume(volume: Float) = settingsRepository.setMasterVolume(volume)
}
