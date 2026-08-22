@file:OptIn(ExperimentalForeignApi::class)

package com.pascal.noctra

import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter
import cocoapods.FirebaseMessaging.FIRMessaging
import com.pascal.noctra.di.audioModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.apps
import dev.gitlive.firebase.initialize
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionModeDefault
import platform.AVFAudio.setActive
import platform.Foundation.NSData
import com.pascal.noctra.di.initKoin as startKoin

fun initKoin() {
    configureIosFirebaseIfNeeded()
    configureAudioSession()

    startKoin(
        platformModules = listOf(audioModule)
    ) {
    }

    Logger.setLogWriters(platformLogWriter())
    Logger.i("Kermit iOS initialized")
}

private fun configureAudioSession() {
    val session = AVAudioSession.sharedInstance()
    try {
        session.setCategory(AVAudioSessionCategoryPlayback, error = null)
        session.setMode(AVAudioSessionModeDefault, error = null)
        session.setActive(true, error = null)
    } catch (e: Exception) {
        Logger.e("Failed to configure audio session: ${e.message}")
    }
}

fun configureIosFirebaseIfNeeded() {
    if (Firebase.apps().isEmpty()) {
        Firebase.initialize()
    }
}

fun setIosFirebaseApnsToken(deviceToken: NSData) {
    configureIosFirebaseIfNeeded()
    FIRMessaging.messaging().APNSToken = deviceToken
}
