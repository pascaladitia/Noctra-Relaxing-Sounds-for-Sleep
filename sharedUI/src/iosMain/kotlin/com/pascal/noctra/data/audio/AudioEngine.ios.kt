package com.pascal.noctra.data.audio

import com.pascal.noctra.domain.model.sound.ActiveSound
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionModeDefault
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSNumber
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.MediaPlayer.MPNowPlayingInfoCenter
import platform.MediaPlayer.MPNowPlayingInfoPropertyElapsedPlaybackTime
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackDuration
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackRate
import platform.MediaPlayer.MPRemoteCommandCenter
import platform.MediaPlayer.MPRemoteCommandHandlerStatus

class IosAudioEngine : AudioEngine {

    private val players = mutableMapOf<String, AVAudioPlayer>()
    private val _activeSounds = mutableMapOf<String, ActiveSound>()
    private var masterVolume = 0.8f
    private var _backgroundPlaybackEnabled = true
    private val soundFileManager = SoundFileManager()
    private val scope = CoroutineScope(Dispatchers.Default)

    override val isBackgroundPlaybackEnabled: Boolean get() = _backgroundPlaybackEnabled
    override fun setBackgroundPlaybackEnabled(enabled: Boolean) { _backgroundPlaybackEnabled = enabled }

    override fun playSound(activeSound: ActiveSound) {
        _activeSounds[activeSound.sound.id] = activeSound
        val existing = players[activeSound.sound.id]
        if (existing != null && existing.isPlaying) return

        scope.launch {
            try {
                configureAudioSession()
                setupRemoteCommandCenter()

                val fileInfo = soundFileManager.getSoundData(activeSound.sound.id, activeSound.sound.fileName)
                val filePath = fileInfo.filePath

                if (filePath != null) {
                    withContext(Dispatchers.Main) {
                        playFromFile(activeSound, filePath)
                        updateNowPlayingInfo(activeSound)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
