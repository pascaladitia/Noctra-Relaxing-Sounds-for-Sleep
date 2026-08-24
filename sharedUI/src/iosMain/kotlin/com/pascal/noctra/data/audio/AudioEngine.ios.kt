package com.pascal.noctra.data.audio

import com.pascal.noctra.domain.model.sound.ActiveSound
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionModeDefault
import platform.Foundation.NSURL
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.create
import platform.MediaPlayer.MPNowPlayingInfoCenter
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackDuration
import platform.MediaPlayer.MPNowPlayingInfoPropertyElapsedPlaybackTime
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackRate
import platform.MediaPlayer.MPRemoteCommand
import platform.MediaPlayer.MPRemoteCommandCenter
import platform.MediaPlayer.MPRemoteCommandHandlerStatus
import kotlin.random.Random

class IosAudioEngine : AudioEngine {

    private val players = mutableMapOf<String, AVAudioPlayer>()
    private val _activeSounds = mutableMapOf<String, ActiveSound>()
    private var masterVolume = 0.8f
    private var _backgroundPlaybackEnabled = true
    private val soundFileManager = SoundFileManager()

    override val isBackgroundPlaybackEnabled: Boolean get() = _backgroundPlaybackEnabled
    override fun setBackgroundPlaybackEnabled(enabled: Boolean) { _backgroundPlaybackEnabled = enabled }

    override fun playSound(activeSound: ActiveSound) {
        _activeSounds[activeSound.sound.id] = activeSound
        val existing = players[activeSound.sound.id]
        if (existing != null && existing.isPlaying) return

        try {
            configureAudioSession()
            setupRemoteCommandCenter()

            val cachedPath = soundFileManager.getCachedPath(activeSound.sound.id)
            if (cachedPath != null) {
                playFromFile(activeSound, cachedPath)
                updateNowPlayingInfo(activeSound)
                return
            }

            playFromGenerated(activeSound)
            updateNowPlayingInfo(activeSound)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playFromFile(activeSound: ActiveSound, filePath: String) {
        val url = NSURL.fileURLWithPath(filePath)
        val player = AVAudioPlayer(contentsOfURL = url, error = null)
        player.numberOfLoops = -1
        player.volume = activeSound.volume * masterVolume
        player.prepareToPlay()
        player.play()
        players[activeSound.sound.id] = player
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun playFromGenerated(activeSound: ActiveSound) {
        val sampleRate = 44100
        val durationSeconds = 30
        val totalSamples = sampleRate * durationSeconds
        val pcmData = ShortArray(totalSamples)
        val random = Random.Default
        var phase = 0.0

        phase = SoundGenerator.generate(activeSound.sound.fileName, pcmData, sampleRate, phase, random)

        val wavData = createWavData(pcmData, sampleRate, 1, 16)
        val tempDir = NSTemporaryDirectory()
        val filePath = "$tempDir/${activeSound.sound.id}_generated.wav"

        wavData.usePinned { pinned ->
            val nsData = kotlinx.cinterop.ExperimentalForeignApi.let {
                platform.Foundation.NSData.create(
                    bytes = pinned.address,
                    length = wavData.size.toULong()
                )
            }
            nsData.writeToFile(filePath, atomically = true)
        }

        val url = NSURL.fileURLWithPath(filePath)
        val player = AVAudioPlayer(contentsOfURL = url, error = null)
        player.numberOfLoops = -1
        player.volume = activeSound.volume * masterVolume
        player.prepareToPlay()
        player.play()
        players[activeSound.sound.id] = player
    }

    private fun setupRemoteCommandCenter() {
        val commandCenter = MPRemoteCommandCenter.sharedCommandCenter

        commandCenter.playCommand.addTargetWithHandler { _ ->
            resumeAll()
            MPRemoteCommandHandlerStatus.success
        }

        commandCenter.pauseCommand.addTargetWithHandler { _ ->
            pauseAll()
            MPRemoteCommandHandlerStatus.success
        }

        commandCenter.togglePlayPauseCommand.addTargetWithHandler { _ ->
            togglePlayPause()
            MPRemoteCommandHandlerStatus.success
        }

        commandCenter.stopCommand.addTargetWithHandler { _ ->
            stopAllSounds()
            MPRemoteCommandHandlerStatus.success
        }

        commandCenter.nextTrackCommand.enabled = false
        commandCenter.previousTrackCommand.enabled = false
    }

    private fun updateNowPlayingInfo(activeSound: ActiveSound) {
        val nowPlayingInfo = mapOf(
            "noctra_title" to activeSound.sound.name,
            "noctra_artist" to "Noctra",
            MPNowPlayingInfoPropertyPlaybackRate to 1.0,
            MPNowPlayingInfoPropertyElapsedPlaybackTime to 0.0,
            MPNowPlayingInfoPropertyPlaybackDuration to 0.0
        )
        MPNowPlayingInfoCenter.defaultCenter.nowPlayingInfo = nowPlayingInfo
    }

    private fun togglePlayPause() {
        val anyPlaying = players.values.any { it.isPlaying }
        if (anyPlaying) {
            pauseAll()
        } else {
            resumeAll()
        }
    }

    private fun pauseAll() {
        players.values.forEach { it.pause() }
        MPNowPlayingInfoCenter.defaultCenter.nowPlayingInfo?.let { info ->
            val mutableInfo = info.toMutableMap()
            mutableInfo[MPNowPlayingInfoPropertyPlaybackRate] = 0.0
            MPNowPlayingInfoCenter.defaultCenter.nowPlayingInfo = mutableInfo
        }
    }

    private fun resumeAll() {
        players.values.forEach { it.play() }
        MPNowPlayingInfoCenter.defaultCenter.nowPlayingInfo?.let { info ->
            val mutableInfo = info.toMutableMap()
            mutableInfo[MPNowPlayingInfoPropertyPlaybackRate] = 1.0
            MPNowPlayingInfoCenter.defaultCenter.nowPlayingInfo = mutableInfo
        }
    }

    private fun createWavData(pcm: ShortArray, sampleRate: Int, channels: Int, bitsPerSample: Int): ByteArray {
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val blockAlign = channels * bitsPerSample / 8
        val dataSize = pcm.size * 2
        val totalSize = 44 + dataSize

        val output = ByteArray(totalSize)

        output[0] = 'R'.code.toByte(); output[1] = 'I'.code.toByte(); output[2] = 'F'.code.toByte(); output[3] = 'F'.code.toByte()
        writeIntLE(output, 4, totalSize - 8)
        output[8] = 'W'.code.toByte(); output[9] = 'A'.code.toByte(); output[10] = 'V'.code.toByte(); output[11] = 'E'.code.toByte()
        output[12] = 'f'.code.toByte(); output[13] = 'm'.code.toByte(); output[14] = 't'.code.toByte(); output[15] = ' '.code.toByte()
        writeIntLE(output, 16, 16)
        writeShortLE(output, 20, 1)
        writeShortLE(output, 22, channels.toShort())
        writeIntLE(output, 24, sampleRate)
        writeIntLE(output, 28, byteRate)
        writeShortLE(output, 32, blockAlign.toShort())
        writeShortLE(output, 34, bitsPerSample.toShort())
        output[36] = 'd'.code.toByte(); output[37] = 'a'.code.toByte(); output[38] = 't'.code.toByte(); output[39] = 'a'.code.toByte()
        writeIntLE(output, 40, dataSize)

        for (i in pcm.indices) {
            val offset = 44 + i * 2
            output[offset] = (pcm[i].toInt() and 0xFF).toByte()
            output[offset + 1] = ((pcm[i].toInt() shr 8) and 0xFF).toByte()
        }

        return output
    }

    private fun writeIntLE(data: ByteArray, offset: Int, value: Int) {
        data[offset] = (value and 0xFF).toByte()
        data[offset + 1] = ((value shr 8) and 0xFF).toByte()
        data[offset + 2] = ((value shr 16) and 0xFF).toByte()
        data[offset + 3] = ((value shr 24) and 0xFF).toByte()
    }

    private fun writeShortLE(data: ByteArray, offset: Int, value: Short) {
        data[offset] = (value.toInt() and 0xFF).toByte()
        data[offset + 1] = ((value.toInt() shr 8) and 0xFF).toByte()
    }

    override fun stopSound(soundId: String) {
        _activeSounds.remove(soundId)
        players.remove(soundId)?.apply { stop() }
        if (players.isEmpty()) {
            MPNowPlayingInfoCenter.defaultCenter.nowPlayingInfo = null
        }
    }

    override fun stopAllSounds() {
        players.forEach { (_, player) -> player.stop() }
        players.clear()
        MPNowPlayingInfoCenter.defaultCenter.nowPlayingInfo = null
    }

    override fun updateVolume(soundId: String, volume: Float) {
        _activeSounds[soundId]?.let { _activeSounds[soundId] = it.copy(volume = volume) }
        players[soundId]?.volume = volume
    }

    override fun setMuted(soundId: String, muted: Boolean) {
        players[soundId]?.volume = if (muted) 0f else (_activeSounds[soundId]?.volume ?: 1f) * masterVolume
    }

    override fun setMasterVolume(volume: Float) {
        masterVolume = volume
        players.forEach { (id, player) ->
            player.volume = (_activeSounds[id]?.volume ?: 1f) * volume
        }
    }

    override fun isPlaying(soundId: String) = players[soundId]?.isPlaying == true
    override fun getActiveSoundIds() = players.keys.toList()

    override fun release() {
        stopAllSounds()
        _activeSounds.clear()
    }

    private fun configureAudioSession() {
        try {
            val session = AVAudioSession.sharedInstance()
            session.setCategory(AVAudioSessionCategoryPlayback, error = null)
            session.setMode(AVAudioSessionModeDefault, error = null)
            session.setActive(true, error = null)
        } catch (_: Exception) {
        }
    }
}
