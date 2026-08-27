package com.pascal.noctra.data.audio

import android.content.Context
import android.content.Intent
import android.os.Build
import com.pascal.noctra.domain.model.sound.ActiveSound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class AndroidAudioEngine(
    private val context: Context
) : AudioEngine {

    private val _activeSounds = ConcurrentHashMap<String, ActiveSound>()
    private var masterVolume = 0.8f
    private var _backgroundPlaybackEnabled = true
    private val soundFileManager = SoundFileManager(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    override val isBackgroundPlaybackEnabled: Boolean get() = _backgroundPlaybackEnabled
    override fun setBackgroundPlaybackEnabled(enabled: Boolean) { _backgroundPlaybackEnabled = enabled }

    override fun playSound(activeSound: ActiveSound) {
        _activeSounds[activeSound.sound.id] = activeSound

        scope.launch {
            val fileInfo = soundFileManager.getSoundData(activeSound.sound.id, activeSound.sound.fileName)
            val filePath = fileInfo.filePath

            if (filePath != null) {
                withContext(Dispatchers.Main) {
                    val service = NoctraPlaybackService.getInstance()
                    if (service != null) {
                        service.playSoundFromFile(
                            activeSound.sound.id,
                            filePath,
                            activeSound.sound.name,
                            activeSound.volume
                        )
                    } else {
                        startServiceWithSound(activeSound, filePath)
                    }
                }
            }
        }
    }

    private fun startServiceWithSound(activeSound: ActiveSound, filePath: String) {
        val serviceIntent = Intent(context, NoctraPlaybackService::class.java).apply {
            action = NoctraPlaybackService.ACTION_START
            putExtra(NoctraPlaybackService.EXTRA_SOUND_ID, activeSound.sound.id)
            putExtra(NoctraPlaybackService.EXTRA_SOUND_FILE_PATH, filePath)
            putExtra(NoctraPlaybackService.EXTRA_SOUND_NAME, activeSound.sound.name)
            putExtra(NoctraPlaybackService.EXTRA_VOLUME, activeSound.volume)
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        } catch (_: Exception) {
        }
    }

    override fun stopSound(soundId: String) {
        _activeSounds.remove(soundId)
        NoctraPlaybackService.getInstance()?.stopSound(soundId)
    }

    override fun stopAllSounds() {
        NoctraPlaybackService.getInstance()?.stopAllAndRemoveNotification()
    }

    override fun updateVolume(soundId: String, volume: Float) {
        _activeSounds[soundId]?.let { _activeSounds[soundId] = it.copy(volume = volume) }
        NoctraPlaybackService.getInstance()?.updateSoundVolume(soundId, volume)
    }

    override fun setMuted(soundId: String, muted: Boolean) {
        NoctraPlaybackService.getInstance()?.updateSoundVolume(
            soundId,
            if (muted) 0f else (_activeSounds[soundId]?.volume ?: 1f) * masterVolume
        )
    }

    override fun setMasterVolume(volume: Float) {
        masterVolume = volume
        NoctraPlaybackService.getInstance()?.setMasterVolume(volume)
    }

    override fun isPlaying(soundId: String): Boolean {
        return NoctraPlaybackService.getInstance()?.isPlaying(soundId) == true
    }

    override fun getActiveSoundIds(): List<String> {
        return NoctraPlaybackService.getInstance()?.getActiveSoundIds() ?: emptyList()
    }

    override fun release() {
        NoctraPlaybackService.getInstance()?.stopAllAndRemoveNotification()
        _activeSounds.clear()
    }
}
