package com.pascal.noctra.data.session

import com.pascal.noctra.data.preferences.PrefLogin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SessionManager {
    private val _sessionExpiredEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionExpiredEvents = _sessionExpiredEvents.asSharedFlow()

    fun notifySessionExpired() {
        _sessionExpiredEvents.tryEmit(Unit)
    }

    fun clearLocalSession() {
        PrefLogin.clear()
    }
}
