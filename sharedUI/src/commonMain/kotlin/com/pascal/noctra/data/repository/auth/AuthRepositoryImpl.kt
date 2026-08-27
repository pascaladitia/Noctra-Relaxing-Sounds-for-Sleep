package com.pascal.noctra.data.repository.auth

import com.pascal.noctra.data.firebase.FirebaseAuthClient
import com.pascal.noctra.data.preferences.PrefLogin
import com.pascal.noctra.domain.model.auth.AuthUser

class AuthRepositoryImpl(
    private val firebaseAuthClient: FirebaseAuthClient
) : AuthRepository {

    override fun initialize() {
        firebaseAuthClient.initialize()
    }

    override suspend fun loginWithEmail(email: String, password: String): AuthUser {
        return firebaseAuthClient.signInWithEmail(email, password).also {
            PrefLogin.setIsLogin(true)
        }
    }

    override suspend fun registerWithEmail(email: String, password: String): AuthUser {
        return firebaseAuthClient.signUpWithEmail(email, password).also {
            PrefLogin.setIsLogin(true)
        }
    }

    override suspend fun getCurrentUser(): AuthUser? {
        return firebaseAuthClient.getCurrentUser()
    }

    override suspend fun getNotificationToken(): String? =
        firebaseAuthClient.refreshNotificationToken()

    override suspend fun logout() {
        firebaseAuthClient.signOut()
        PrefLogin.setIsLogin(false)
    }
}
