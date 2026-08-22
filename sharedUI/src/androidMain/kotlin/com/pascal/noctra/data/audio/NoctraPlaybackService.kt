package com.pascal.noctra.data.audio

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.session.MediaButtonReceiver

class NoctraPlaybackService : Service() {

    private var mediaSession: MediaSessionCompat? = null
    private val exoPlayers = mutableMapOf<String, android.media.MediaPlayer>()
    private var masterVolume = 0.8f
    private var isServiceForeground = false

    companion object {
        const val ACTION_START = "com.pascal.noctra.START_PLAYBACK"
        const val ACTION_STOP = "com.pascal.noctra.STOP_PLAYBACK"
        const val ACTION_TOGGLE = "com.pascal.noctra.TOGGLE_PLAYBACK"
        const val EXTRA_SOUND_ID = "sound_id"
        const val EXTRA_SOUND_FILE_PATH = "sound_file_path"
        const val EXTRA_SOUND_NAME = "sound_name"
        const val EXTRA_VOLUME = "volume"
        const val NOTIFICATION_ID = 1001

        private var instance: NoctraPlaybackService? = null
        fun getInstance(): NoctraPlaybackService? = instance
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        val notification = AudioNotificationHelper.buildPlaybackNotification(
            context = this,
            activeCount = 0,
            isPlaying = false,
            mediaSession = null
        )
        startForeground(NOTIFICATION_ID, notification)
        isServiceForeground = true
        initMediaSession()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action != null) {
            MediaButtonReceiver.handleIntent(mediaSession, intent)
        }

        when (intent?.action) {
            ACTION_START -> {
                val soundId = intent.getStringExtra(EXTRA_SOUND_ID) ?: return START_NOT_STICKY
                val filePath = intent.getStringExtra(EXTRA_SOUND_FILE_PATH)
                val soundName = intent.getStringExtra(EXTRA_SOUND_NAME) ?: "Noctra"
                val volume = intent.getFloatExtra(EXTRA_VOLUME, 0.8f)
                startSoundPlayback(soundId, filePath, soundName, volume)
            }
            ACTION_STOP -> {
                stopAllAndRemoveNotification()
            }
            ACTION_TOGGLE -> {
                togglePlayPause()
            }
        }

        return START_NOT_STICKY
    }

    private fun initMediaSession() {
        val session = MediaSessionCompat(this, "NoctraPlayback").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    togglePlayPause()
                }

                override fun onPause() {
                    pauseAll()
                }

                override fun onStop() {
                    stopAllAndRemoveNotification()
                }

                override fun onSkipToNext() {
                    togglePlayPause()
                }

                override fun onSkipToPrevious() {
                    togglePlayPause()
                }
            })
            isActive = true
        }
        mediaSession = session
    }

    private fun startSoundPlayback(soundId: String, filePath: String?, soundName: String, volume: Float) {
        try {
            val existing = exoPlayers[soundId]
            if (existing != null) {
                if (!existing.isPlaying) {
                    existing.setVolume(volume * masterVolume, volume * masterVolume)
                    existing.start()
                }
                updateNotification(soundName, true)
                return
            }

            if (filePath == null) return

            val player = android.media.MediaPlayer().apply {
                setDataSource(filePath)
                isLooping = true
                setVolume(volume * masterVolume, volume * masterVolume)
                prepare()
                start()
            }

            exoPlayers[soundId] = player
            updateNotification(soundName, true)
            updateMediaSessionMetadata(soundName, exoPlayers.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playSoundFromFile(soundId: String, filePath: String, soundName: String, volume: Float) {
        startSoundPlayback(soundId, filePath, soundName, volume)
    }

    fun stopSound(soundId: String) {
        exoPlayers.remove(soundId)?.apply {
            try {
                stop()
                release()
            } catch (_: Exception) {}
        }
        if (exoPlayers.isEmpty()) {
            stopSelfRemoveNotification()
        } else {
            updateMediaSessionMetadata("Noctra", exoPlayers.size)
        }
    }

    fun updateSoundVolume(soundId: String, volume: Float) {
        try {
            exoPlayers[soundId]?.setVolume(volume * masterVolume, volume * masterVolume)
        } catch (_: Exception) {}
    }

    fun setMasterVolume(volume: Float) {
        masterVolume = volume
        exoPlayers.values.forEach { player ->
            try {
                player.setVolume(volume, volume)
            } catch (_: Exception) {}
        }
    }

    fun isPlaying(soundId: String): Boolean {
        return try {
            exoPlayers[soundId]?.isPlaying == true
        } catch (_: Exception) {
            false
        }
    }

    fun hasActivePlayers(): Boolean = exoPlayers.isNotEmpty()

    private fun togglePlayPause() {
        val anyPlaying = exoPlayers.values.any { try { it.isPlaying } catch (_: Exception) { false } }
        if (anyPlaying) {
            pauseAll()
        } else {
            resumeAll()
        }
    }

    private fun pauseAll() {
        exoPlayers.values.forEach { player ->
            try {
                if (player.isPlaying) player.pause()
            } catch (_: Exception) {}
        }
        updateMediaSessionState(false)
    }

    private fun resumeAll() {
        exoPlayers.values.forEach { player ->
            try {
                player.start()
            } catch (_: Exception) {}
        }
        updateMediaSessionState(true)
    }

    fun stopAllAndRemoveNotification() {
        exoPlayers.values.forEach { player ->
            try {
                player.stop()
                player.release()
            } catch (_: Exception) {}
        }
        exoPlayers.clear()
        stopSelfRemoveNotification()
    }

    private fun stopSelfRemoveNotification() {
        if (isServiceForeground) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            isServiceForeground = false
        }
        stopSelf()
    }

    private fun updateNotification(soundName: String, isPlaying: Boolean) {
        val activeCount = exoPlayers.size
        val notification = AudioNotificationHelper.buildPlaybackNotification(
            context = this,
            activeCount = activeCount,
            isPlaying = isPlaying,
            mediaSession = mediaSession
        )

        startForeground(NOTIFICATION_ID, notification)
        isServiceForeground = true
    }

    private fun updateMediaSessionMetadata(soundName: String, trackCount: Int) {
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, soundName)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Noctra")
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "$trackCount sound${if (trackCount != 1) "s" else ""} active")
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, -1L)
            .build()

        mediaSession?.setMetadata(metadata)
    }

    private fun updateMediaSessionState(isPlaying: Boolean) {
        val state = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_STOP or
                PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
            .setState(
                if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
                1.0f
            )
            .build()

        mediaSession?.setPlaybackState(state)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopAllAndRemoveNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAllAndRemoveNotification()
        mediaSession?.release()
        mediaSession = null
        instance = null
    }
}
