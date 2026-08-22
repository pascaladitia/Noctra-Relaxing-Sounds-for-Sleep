package com.pascal.noctra.data.audio

import com.pascal.noctra.domain.model.mixer.MixerState
import com.pascal.noctra.domain.model.sound.ActiveSound
import com.pascal.noctra.domain.model.timer.SleepTimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface AudioNotificationCallback {
    fun onPlaybackStateChanged(isPlaying: Boolean, activeCount: Int)
}

class AudioPlayerManager(
    private val audioEngine: AudioEngine
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _mixerState = MutableStateFlow(MixerState())
    val mixerState: StateFlow<MixerState> = _mixerState.asStateFlow()

    private var timerJob: Job? = null
    var notificationCallback: AudioNotificationCallback? = null

    fun addSound(activeSound: ActiveSound) {
        val current = _mixerState.value
        if (current.activeSounds.any { it.sound.id == activeSound.sound.id }) return

        val updated = current.copy(
            activeSounds = current.activeSounds + activeSound,
            isPlaying = true
        )
        _mixerState.value = updated
        audioEngine.playSound(activeSound)
        notifyNotification()
    }

    fun removeSound(soundId: String) {
        val current = _mixerState.value
        val updated = current.copy(
            activeSounds = current.activeSounds.filter { it.sound.id != soundId }
        )
        _mixerState.value = updated
        audioEngine.stopSound(soundId)
        notifyNotification()
    }

    fun updateSoundVolume(soundId: String, volume: Float) {
        val current = _mixerState.value
        val updated = current.copy(
            activeSounds = current.activeSounds.map {
                if (it.sound.id == soundId) it.copy(volume = volume) else it
            }
        )
        _mixerState.value = updated
        audioEngine.updateVolume(soundId, volume * current.masterVolume)
    }

    fun toggleMute(soundId: String) {
        val current = _mixerState.value
        val sound = current.activeSounds.find { it.sound.id == soundId } ?: return
        val newMuted = !sound.isMuted
        val updated = current.copy(
            activeSounds = current.activeSounds.map {
                if (it.sound.id == soundId) it.copy(isMuted = newMuted) else it
            }
        )
        _mixerState.value = updated
        audioEngine.setMuted(soundId, newMuted)
    }

    fun setMasterVolume(volume: Float) {
        _mixerState.value = _mixerState.value.copy(masterVolume = volume)
        audioEngine.setMasterVolume(volume)
        _mixerState.value.activeSounds.forEach { sound ->
            audioEngine.updateVolume(sound.sound.id, sound.volume * volume)
        }
    }

    fun togglePlayPause() {
        val current = _mixerState.value
        if (current.isPlaying) {
            pauseAll()
        } else {
            resumeAll()
        }
        notifyNotification()
    }

    fun stopAllSounds() {
        pauseAll()
        _mixerState.value = _mixerState.value.copy(activeSounds = emptyList())
        audioEngine.release()
        notifyNotification()
    }

    private fun pauseAll() {
        val current = _mixerState.value
        _mixerState.value = current.copy(isPlaying = false)
        audioEngine.stopAllSounds()
    }

    private fun resumeAll() {
        val current = _mixerState.value
        _mixerState.value = current.copy(isPlaying = true)
        current.activeSounds.forEach { audioEngine.playSound(it) }
    }

    fun loadPresetSounds(sounds: List<ActiveSound>) {
        audioEngine.stopAllSounds()
        val updated = MixerState(
            activeSounds = sounds,
            isPlaying = true,
            masterVolume = _mixerState.value.masterVolume
        )
        _mixerState.value = updated
        sounds.forEach { audioEngine.playSound(it) }
        notifyNotification()
    }

    fun startSleepTimer(durationMs: Long) {
        timerJob?.cancel()
        _mixerState.value = _mixerState.value.copy(
            sleepTimer = SleepTimerState(
                isActive = true,
                totalDurationMs = durationMs,
                remainingMs = durationMs
            )
        )
        timerJob = scope.launch {
            while (_mixerState.value.sleepTimer.isActive && _mixerState.value.sleepTimer.remainingMs > 0) {
                delay(1000)
                _mixerState.update { state ->
                    val newRemaining = state.sleepTimer.remainingMs - 1000
                    val timer = state.sleepTimer.copy(remainingMs = newRemaining)

                    if (newRemaining <= 30_000L && newRemaining > 0) {
                        val fadeProgress = (30_000L - newRemaining).toFloat() / 30_000L
                        val newVolume = state.masterVolume * (1f - fadeProgress)
                        audioEngine.setMasterVolume(newVolume)
                        state.copy(sleepTimer = timer, masterVolume = newVolume)
                    } else if (newRemaining <= 0) {
                        audioEngine.stopAllSounds()
                        state.copy(
                            sleepTimer = SleepTimerState(),
                            isPlaying = false
                        )
                    } else {
                        state.copy(sleepTimer = timer)
                    }
                }
            }
            notifyNotification()
        }
    }

    fun cancelSleepTimer() {
        timerJob?.cancel()
        val originalVolume = 0.8f
        audioEngine.setMasterVolume(originalVolume)
        _mixerState.value = _mixerState.value.copy(
            sleepTimer = SleepTimerState(),
            masterVolume = originalVolume
        )
    }

    fun release() {
        timerJob?.cancel()
        audioEngine.release()
        _mixerState.value = MixerState()
        notifyNotification()
    }

    private fun notifyNotification() {
        val state = _mixerState.value
        notificationCallback?.onPlaybackStateChanged(state.isPlaying, state.activeSoundCount)
    }
}
