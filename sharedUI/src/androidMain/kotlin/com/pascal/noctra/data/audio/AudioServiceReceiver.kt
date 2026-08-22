package com.pascal.noctra.data.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AudioServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val manager = AudioPlayerManagerHolder.manager ?: return

        when (intent.getStringExtra("action")) {
            AudioNotificationHelper.ACTION_TOGGLE_PLAY_PAUSE -> {
                manager.togglePlayPause()
            }
            AudioNotificationHelper.ACTION_STOP -> {
                manager.stopAllSounds()
            }
        }
    }
}
