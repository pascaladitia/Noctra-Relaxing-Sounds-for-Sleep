package com.pascal.noctra.data.audio

import android.content.Context
import android.media.MediaPlayer
import com.pascal.noctra.domain.model.sound.ActiveSound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class AndroidAudioEngine(
    private val context: Context
) : AudioEngine {

    private val mediaPlayers = ConcurrentHashMap<String, MediaPlayer>()
    private val _activeSounds = ConcurrentHashMap<String, ActiveSound>()
    private var masterVolume = 0.8f
    private var _backgroundPlaybackEnabled = true
    private val soundFileManager = SoundFileManager(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    override val isBackgroundPlaybackEnabled: Boolean get() = _backgroundPlaybackEnabled
    override fun setBackgroundPlaybackEnabled(enabled: Boolean) { _backgroundPlaybackEnabled = enabled }

    override fun playSound(activeSound: ActiveSound) {
        val existingMedia = mediaPlayers[activeSound.sound.id]
        if (existingMedia != null && existingMedia.isPlaying) return

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
                        playFromFile(activeSound, filePath)
                    }
                }
            }
        }
    }

    private fun playFromFile(activeSound: ActiveSound, filePath: String) {
        try {
            val player = MediaPlayer().apply {
                setDataSource(filePath)
                isLooping = true
                setVolume(
                    activeSound.volume * masterVolume,
                    activeSound.volume * masterVolume
                )
                prepare()
                start()
            }
            mediaPlayers[activeSound.sound.id] = player
        } catch (_: Exception) {
        }
    }

    override fun stopSound(soundId: String) {
        _activeSounds.remove(soundId)
        mediaPlayers.remove(soundId)?.apply {
            try { stop(); release() } catch (_: Exception) {}
        }
        NoctraPlaybackService.getInstance()?.stopSound(soundId)
    }

    override fun stopAllSounds() {
        mediaPlayers.forEach { (_, player) ->
            try { player.stop(); player.release() } catch (_: Exception) {}
        }
        mediaPlayers.clear()
        NoctraPlaybackService.getInstance()?.stopAllAndRemoveNotification()
    }

    override fun updateVolume(soundId: String, volume: Float) {
        _activeSounds[soundId]?.let { _activeSounds[soundId] = it.copy(volume = volume) }
        try { mediaPlayers[soundId]?.setVolume(volume * masterVolume, volume * masterVolume) } catch (_: Exception) {}
        NoctraPlaybackService.getInstance()?.updateSoundVolume(soundId, volume)
    }

    override fun setMuted(soundId: String, muted: Boolean) {
        try {
            if (muted) {
                mediaPlayers[soundId]?.setVolume(0f, 0f)
            } else {
                val s = _activeSounds[soundId]
                val v = if (s != null) s.volume * masterVolume else masterVolume
                mediaPlayers[soundId]?.setVolume(v, v)
            }
        } catch (_: Exception) {}
        NoctraPlaybackService.getInstance()?.updateSoundVolume(
            soundId,
            if (muted) 0f else (_activeSounds[soundId]?.volume ?: 1f) * masterVolume
        )
    }

    override fun setMasterVolume(volume: Float) {
        masterVolume = volume
        mediaPlayers.forEach { (id, player) ->
            val v = (_activeSounds[id]?.volume ?: 1f) * volume
            try { player.setVolume(v, v) } catch (_: Exception) {}
        }
        NoctraPlaybackService.getInstance()?.setMasterVolume(volume)
    }

    override fun isPlaying(soundId: String): Boolean {
        val mediaPlayerPlaying = try { mediaPlayers[soundId]?.isPlaying == true } catch (_: Exception) { false }
        val servicePlaying = NoctraPlaybackService.getInstance()?.isPlaying(soundId) == true
        return mediaPlayerPlaying || servicePlaying
    }

    override fun getActiveSoundIds(): List<String> {
        return mediaPlayers.keys.toList()
    }

    override fun release() {
        stopAllSounds()
        _activeSounds.clear()
    }
}
