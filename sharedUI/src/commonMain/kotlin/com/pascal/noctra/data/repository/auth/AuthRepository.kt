package com.pascal.noctra.data.repository.auth

import com.pascal.noctra.domain.model.auth.AuthUser

interface AuthRepository {
    fun initialize()
    suspend fun loginWithEmail(email: String, password: String): AuthUser
    suspend fun registerWithEmail(email: String, password: String): AuthUser
    suspend fun getCurrentUser(): AuthUser?
    suspend fun getNotificationToken(): String?
    suspend fun logout()
}
