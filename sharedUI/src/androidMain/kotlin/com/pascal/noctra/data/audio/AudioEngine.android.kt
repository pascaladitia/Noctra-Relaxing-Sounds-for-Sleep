package com.pascal.noctra.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.pascal.noctra.domain.model.sound.ActiveSound
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class AndroidAudioEngine(
    private val context: Context
) : AudioEngine {

    private val players = ConcurrentHashMap<String, AudioTrack>()
    private val threads = ConcurrentHashMap<String, Thread>()
    private val _activeSounds = ConcurrentHashMap<String, ActiveSound>()
    private var masterVolume = 0.8f
    private var _backgroundPlaybackEnabled = true

    override val isBackgroundPlaybackEnabled: Boolean get() = _backgroundPlaybackEnabled
    override fun setBackgroundPlaybackEnabled(enabled: Boolean) { _backgroundPlaybackEnabled = enabled }

    override fun playSound(activeSound: ActiveSound) {
        val existing = players[activeSound.sound.id]
        if (existing != null) {
            if (existing.playState != AudioTrack.PLAYSTATE_PLAYING) existing.play()
            return
        }

        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)

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

        _activeSounds[activeSound.sound.id] = activeSound

        val thread = Thread {
            try {
                audioTrack.setVolume(activeSound.volume * masterVolume)
                audioTrack.play()
                val buf = ShortArray(4096)
                var phase = 0.0
                val random = Random.Default
                val genState = SoundGeneratorState()
                while (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    phase = SoundGenerator.generate(activeSound.sound.fileName, buf, sampleRate, phase, random, genState)
                    audioTrack.write(buf, 0, buf.size)
                }
            } catch (_: Exception) {}
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
    }

    override fun stopAllSounds() {
        players.forEach { (_, player) ->
            try { player.stop(); player.release() } catch (_: Exception) {}
        }
        players.clear()
        threads.clear()
    }

    override fun updateVolume(soundId: String, volume: Float) {
        _activeSounds[soundId]?.let { _activeSounds[soundId] = it.copy(volume = volume) }
        try { players[soundId]?.setVolume(volume) } catch (_: Exception) {}
    }

    override fun setMuted(soundId: String, muted: Boolean) {
        try {
            if (muted) {
                players[soundId]?.setVolume(0f)
            } else {
                val s = _activeSounds[soundId]
                val v = if (s != null) s.volume * masterVolume else masterVolume
                players[soundId]?.setVolume(v)
            }
        } catch (_: Exception) {}
    }

    override fun setMasterVolume(volume: Float) {
        masterVolume = volume
        players.forEach { (id, player) ->
            val v = (_activeSounds[id]?.volume ?: 1f) * volume
            try { player.setVolume(v) } catch (_: Exception) {}
        }
    }

    override fun isPlaying(soundId: String) = players[soundId]?.playState == AudioTrack.PLAYSTATE_PLAYING
    override fun getActiveSoundIds() = players.keys.toList()

    override fun release() {
        stopAllSounds()
        _activeSounds.clear()
    }
}
