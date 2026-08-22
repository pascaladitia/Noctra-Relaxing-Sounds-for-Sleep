package com.pascal.noctra.androidApp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import com.pascal.noctra.App
import com.pascal.noctra.ContextUtils
import com.pascal.noctra.data.audio.AudioNotificationCallback
import com.pascal.noctra.data.audio.AudioNotificationHelper
import com.pascal.noctra.data.audio.AudioPlayerManager
import com.pascal.noctra.data.audio.AudioPlayerManagerHolder
import com.pascal.noctra.di.audioModule
import com.pascal.noctra.di.initKoin
import com.pascal.noctra.utils.AndroidAppLocaleManager
import com.pascal.noctra.utils.setAppLanguage
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class AppActivity : ComponentActivity(), AudioNotificationCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ContextUtils.setActivity(this)

        val localeCode = AndroidAppLocaleManager(this).getLocale()
        val langName = when (localeCode) {
            "id", "in", "INDONESIA" -> "INDONESIA"
            else -> "ENGLISH"
        }
        setAppLanguage(langName)

        if (GlobalContext.getOrNull() == null) {
            initKoin(
                platformModules = listOf(audioModule)
            ) {
                androidLogger(level = Level.NONE)
                androidContext(this@AppActivity)
            }
        }

        val audioPlayerManager = GlobalContext.get().get<AudioPlayerManager>()
        AudioPlayerManagerHolder.manager = audioPlayerManager
        audioPlayerManager.notificationCallback = this

        setContent {
            App(onThemeChanged = { ThemeChanged(it) })
        }
    }

    override fun onPlaybackStateChanged(isPlaying: Boolean, activeCount: Int) {
        runOnUiThread {
            if (isPlaying && activeCount > 0) {
                AudioNotificationHelper.showNotification(this, isPlaying, activeCount)
            } else {
                AudioNotificationHelper.hideNotification(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioPlayerManagerHolder.manager?.notificationCallback = null
    }
}

@Composable
private fun ThemeChanged(isDark: Boolean) {
    val view = LocalView.current
    LaunchedEffect(isDark) {
        val window = (view.context as Activity).window
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = isDark
            isAppearanceLightNavigationBars = isDark
        }
    }
}
