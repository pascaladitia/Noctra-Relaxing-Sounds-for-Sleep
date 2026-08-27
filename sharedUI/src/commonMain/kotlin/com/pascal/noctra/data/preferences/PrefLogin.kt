package com.pascal.noctra.data.preferences

import com.pascal.noctra.createSettings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

object PrefLogin {
    private const val IS_ONBOARDING = "is_onboarding"
    private const val IS_LOGIN = "is_login"
    private const val RESPONSE_LOGIN = "response_login"

    fun setIsOnboarding(value: Boolean) {
        createSettings()[IS_ONBOARDING] = value
    }

    fun getIsOnboarding(): Boolean {
        return createSettings()[IS_ONBOARDING, false]
    }

    fun setIsLogin(value: Boolean) {
        createSettings()[IS_LOGIN] = value
    }

    fun getIsLogin(): Boolean {
        return createSettings()[IS_LOGIN, false]
    }

    fun clear() {
        createSettings().clear()
    }
}