package com.pascal.noctra.data.repository.settings

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsRepositoryImpl(
    private val settings: Settings
) : SettingsRepository {

    private val backgroundPlayback = MutableStateFlow(settings.getBoolean("background_playback", true))
    private val soundQuality = MutableStateFlow(settings.getString("sound_quality", "high"))
    private val masterVolume = MutableStateFlow(settings.getFloat("master_volume", 0.8f))

    override fun getBackgroundPlaybackEnabled(): Flow<Boolean> = backgroundPlayback
    override fun setBackgroundPlaybackEnabled(enabled: Boolean) {
        settings.putBoolean("background_playback", enabled)
        backgroundPlayback.value = enabled
    }

    override fun getSoundQuality(): Flow<String> = soundQuality
    override fun setSoundQuality(quality: String) {
        settings.putString("sound_quality", quality)
        soundQuality.value = quality
    }

    override fun getOnboardingCompleted(): Boolean = settings.getBoolean("onboarding_completed", false)
    override fun setOnboardingCompleted(completed: Boolean) {
        settings.putBoolean("onboarding_completed", completed)
    }

    override fun getMasterVolume(): Flow<Float> = masterVolume
    override fun setMasterVolume(volume: Float) {
        settings.putFloat("master_volume", volume)
        masterVolume.value = volume
    }
}
