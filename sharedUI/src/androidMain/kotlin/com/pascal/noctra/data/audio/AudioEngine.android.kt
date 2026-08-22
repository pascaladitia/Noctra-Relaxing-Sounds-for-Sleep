package com.pascal.noctra.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import com.pascal.noctra.domain.model.sound.ActiveSound
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class AndroidAudioEngine(
    private val context: Context
) : AudioEngine {

    private val players = ConcurrentHashMap<String, AudioTrack>()
    private val mediaPlayers = ConcurrentHashMap<String, MediaPlayer>()
    private val threads = ConcurrentHashMap<String, Thread>()
    private val _activeSounds = ConcurrentHashMap<String, ActiveSound>()
    private var masterVolume = 0.8f
    private var _backgroundPlaybackEnabled = true
    private val soundFileManager = SoundFileManager(context)

    override val isBackgroundPlaybackEnabled: Boolean get() = _backgroundPlaybackEnabled
    override fun setBackgroundPlaybackEnabled(enabled: Boolean) { _backgroundPlaybackEnabled = enabled }

    override fun playSound(activeSound: ActiveSound) {
        val existingAudio = players[activeSound.sound.id]
        val existingMedia = mediaPlayers[activeSound.sound.id]
        if (existingAudio != null && existingAudio.playState == AudioTrack.PLAYSTATE_PLAYING) return
        if (existingMedia != null && existingMedia.isPlaying) return

        _activeSounds[activeSound.sound.id] = activeSound

        val cachedPath = soundFileManager.getCachedPath(activeSound.sound.id)

        if (cachedPath != null) {
            val service = NoctraPlaybackService.getInstance()
            if (service != null) {
                service.playSoundFromFile(
                    activeSound.sound.id,
                    cachedPath,
                    activeSound.sound.name,
                    activeSound.volume
                )
                return
            }
            playFromFile(activeSound, cachedPath)
            return
        }

        playWithAudioTrack(activeSound)
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

    private fun playWithAudioTrack(activeSound: ActiveSound) {
        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize * 2)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        val thread = Thread {
            try {
                audioTrack.setVolume(activeSound.volume * masterVolume)
                audioTrack.play()
                val buf = ShortArray(4096)
                var phase = 0.0
                val random = Random.Default
                val genState = SoundGeneratorState()
                while (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    phase = SoundGenerator.generate(
                        activeSound.sound.fileName,
                        buf,
                        sampleRate,
                        phase,
                        random,
                        genState
                    )
                    audioTrack.write(buf, 0, buf.size)
                }
            } catch (_: Exception) {
            }
        }
        thread.isDaemon = true
        thread.start()
        players[activeSound.sound.id] = audioTrack
        threads[activeSound.sound.id] = thread
    }

    override fun stopSound(soundId: String) {
        _activeSounds.remove(soundId)
        threads.remove(soundId)
        players.remove(soundId)?.apply {
            try { stop(); release() } catch (_: Exception) {}
        }
        mediaPlayers.remove(soundId)?.apply {
            try { stop(); release() } catch (_: Exception) {}
        }
        NoctraPlaybackService.getInstance()?.stopSound(soundId)
    }

    override fun stopAllSounds() {
        players.forEach { (_, player) ->
            try { player.stop(); player.release() } catch (_: Exception) {}
        }
        mediaPlayers.forEach { (_, player) ->
            try { player.stop(); player.release() } catch (_: Exception) {}
        }
        players.clear()
        mediaPlayers.clear()
        threads.clear()
        NoctraPlaybackService.getInstance()?.stopAllAndRemoveNotification()
    }

    override fun updateVolume(soundId: String, volume: Float) {
        _activeSounds[soundId]?.let { _activeSounds[soundId] = it.copy(volume = volume) }
        try { players[soundId]?.setVolume(volume) } catch (_: Exception) {}
        try { mediaPlayers[soundId]?.setVolume(volume * masterVolume, volume * masterVolume) } catch (_: Exception) {}
        NoctraPlaybackService.getInstance()?.updateSoundVolume(soundId, volume)
    }

    override fun setMuted(soundId: String, muted: Boolean) {
        try {
            if (muted) {
                players[soundId]?.setVolume(0f)
                mediaPlayers[soundId]?.setVolume(0f, 0f)
            } else {
                val s = _activeSounds[soundId]
                val v = if (s != null) s.volume * masterVolume else masterVolume
                players[soundId]?.setVolume(v)
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
        players.forEach { (id, player) ->
            val v = (_activeSounds[id]?.volume ?: 1f) * volume
            try { player.setVolume(v) } catch (_: Exception) {}
        }
        mediaPlayers.forEach { (id, player) ->
            val v = (_activeSounds[id]?.volume ?: 1f) * volume
            try { player.setVolume(v, v) } catch (_: Exception) {}
        }
        NoctraPlaybackService.getInstance()?.setMasterVolume(volume)
    }

    override fun isPlaying(soundId: String): Boolean {
        val audioTrackPlaying = players[soundId]?.playState == AudioTrack.PLAYSTATE_PLAYING
        val mediaPlayerPlaying = try { mediaPlayers[soundId]?.isPlaying == true } catch (_: Exception) { false }
        val servicePlaying = NoctraPlaybackService.getInstance()?.isPlaying(soundId) == true
        return audioTrackPlaying || mediaPlayerPlaying || servicePlaying
    }

    override fun getActiveSoundIds(): List<String> {
        val allIds = mutableSetOf<String>()
        allIds.addAll(players.keys)
        allIds.addAll(mediaPlayers.keys)
        return allIds.toList()
    }

    override fun release() {
        stopAllSounds()
        _activeSounds.clear()
    }
}