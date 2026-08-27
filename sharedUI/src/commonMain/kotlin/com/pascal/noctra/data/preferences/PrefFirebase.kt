package com.pascal.noctra.data.preferences

import com.pascal.noctra.createSettings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

object PrefFirebase {
    private const val FCM_TOKEN = "firebase_fcm_token"

    fun setFcmToken(value: String?) {
        if (value.isNullOrBlank()) return
        createSettings()[FCM_TOKEN] = value
    }

    fun getFcmToken(): String? {
        return createSettings()[FCM_TOKEN]
    }

    fun clearFcmToken() {
        createSettings().remove(FCM_TOKEN)
    }
}
