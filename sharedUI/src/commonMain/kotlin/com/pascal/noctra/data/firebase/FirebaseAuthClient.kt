package com.pascal.noctra.data.firebase

import com.pascal.noctra.data.preferences.PrefFirebase
import com.pascal.noctra.domain.model.auth.AuthUser
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.messaging.messaging

class FirebaseAuthClient {
    private var initialized = false

    fun initialize() {
        if (initialized) return
        Firebase.auth
        initialized = true
    }

    suspend fun signInWithEmail(email: String, password: String): AuthUser {
        val result = Firebase.auth.signInWithEmailAndPassword(email.trim(), password)
        return result.user.toAuthUser().withSavedToken()
    }

    suspend fun signUpWithEmail(email: String, password: String): AuthUser {
        val result = Firebase.auth.createUserWithEmailAndPassword(email.trim(), password)
        val user = result.user
        val displayName = email.substringBefore("@").ifBlank { "Noctra User" }
        user?.updateProfile(displayName = displayName)
        return user.toAuthUser().withSavedToken()
    }

    suspend fun getCurrentUser(): AuthUser? {
        return Firebase.auth.currentUser?.toAuthUser()?.withSavedToken()
    }

    suspend fun refreshNotificationToken(): String? {
        val token = runCatching { Firebase.messaging.getToken() }.getOrNull()
        PrefFirebase.setFcmToken(token)
        return token ?: PrefFirebase.getFcmToken()
    }

    suspend fun signOut() {
        Firebase.auth.signOut()
    }

    private suspend fun AuthUser.withSavedToken(): AuthUser {
        val token = refreshNotificationToken()
        return copy(fcmToken = token)
    }

    private fun FirebaseUser?.toAuthUser(): AuthUser {
        val user = requireNotNull(this) { "Firebase user kosong setelah login." }
        return AuthUser(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName,
            photoUrl = user.photoURL,
            fcmToken = null
        )
    }
}
