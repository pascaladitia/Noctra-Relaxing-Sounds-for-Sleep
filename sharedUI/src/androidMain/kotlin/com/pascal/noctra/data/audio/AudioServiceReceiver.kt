package com.pascal.noctra.data.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AudioServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.getStringExtra("action")

        val serviceIntent = Intent(context, NoctraPlaybackService::class.java).apply {
            this.action = when (action) {
                AudioNotificationHelper.ACTION_TOGGLE_PLAY_PAUSE -> NoctraPlaybackService.ACTION_TOGGLE
                AudioNotificationHelper.ACTION_STOP -> NoctraPlaybackService.ACTION_STOP
                else -> return
            }
        }

        try {
            context.startForegroundService(serviceIntent)
        } catch (_: Exception) {
            try {
                context.startService(serviceIntent)
            } catch (_: Exception) {
            }
        }
    }
}
