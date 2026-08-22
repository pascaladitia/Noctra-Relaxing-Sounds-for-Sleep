package com.pascal.noctra.androidApp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.pascal.noctra.data.audio.AudioNotificationHelper

class MyApplication : Application() {
    companion object {
        lateinit var INSTANCE: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        createNotificationChannel()
        AudioNotificationHelper.createNotificationChannel(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            "Noctra_notification",
            "Noctra Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Noctra notification channel"
        }

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }
}
