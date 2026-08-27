package com.pascal.noctra.data.audio

import com.pascal.noctra.domain.model.sound.ActiveSound

interface AudioEngine {
    fun playSound(activeSound: ActiveSound)
    fun stopSound(soundId: String)
    fun stopAllSounds()
    fun updateVolume(soundId: String, volume: Float)
    fun setMuted(soundId: String, muted: Boolean)
    fun setMasterVolume(volume: Float)
    fun isPlaying(soundId: String): Boolean
    fun getActiveSoundIds(): List<String>
    fun release()
    val isBackgroundPlaybackEnabled: Boolean
    fun setBackgroundPlaybackEnabled(enabled: Boolean)
}
