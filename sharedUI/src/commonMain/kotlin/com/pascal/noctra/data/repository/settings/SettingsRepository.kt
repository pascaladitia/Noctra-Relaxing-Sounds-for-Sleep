package com.pascal.noctra.data.repository.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getBackgroundPlaybackEnabled(): Flow<Boolean>
    fun setBackgroundPlaybackEnabled(enabled: Boolean)
    fun getSoundQuality(): Flow<String>
    fun setSoundQuality(quality: String)
    fun getOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)
    fun getMasterVolume(): Flow<Float>
    fun setMasterVolume(volume: Float)
}
