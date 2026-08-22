package com.pascal.noctra.data.audio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat

object AudioNotificationHelper {
    const val CHANNEL_ID = "noctra_playback"
    const val NOTIFICATION_ID = 1001
    const val ACTION_TOGGLE_PLAY_PAUSE = "toggle_play_pause"
    const val ACTION_STOP = "stop"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Noctra Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controls for background audio playback"
                setShowBadge(false)
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun buildPlaybackNotification(
        context: Context,
        activeCount: Int,
        isPlaying: Boolean
    ): android.app.Notification {
        val playPauseAction = NotificationCompat.Action(
            if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
            if (isPlaying) "Pause" else "Play",
            createActionIntent(context, ACTION_TOGGLE_PLAY_PAUSE)
        )

        val stopAction = NotificationCompat.Action(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Stop",
            createActionIntent(context, ACTION_STOP)
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Noctra")
            .setContentText("$activeCount sound${if (activeCount != 1) "s" else ""} playing")
            .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_lock_silent_mode_off))
            .setOngoing(true)
            .setShowWhen(false)
            .setSilent(true)
            .addAction(playPauseAction)
            .addAction(stopAction)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1)
                    .setMediaSession(null)
            )
            .build()
    }

    fun showNotification(context: Context, isPlaying: Boolean, activeCount: Int) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, buildPlaybackNotification(context, activeCount, isPlaying))
    }

    fun hideNotification(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancel(NOTIFICATION_ID)
    }

    private fun createActionIntent(context: Context, action: String): PendingIntent {
        val intent = Intent("com.pascal.noctra.AUDIO_ACTION").apply {
            putExtra("action", action)
            setPackage(context.packageName)
        }
        return PendingIntent.getBroadcast(
            context, action.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
