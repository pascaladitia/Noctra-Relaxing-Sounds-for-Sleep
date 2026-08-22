package com.pascal.noctra.domain.usecase.remote.auth

import com.pascal.noctra.domain.model.auth.AuthUser

interface AuthUseCase {
    suspend fun loginWithEmail(email: String, password: String): AuthUser
    suspend fun registerWithEmail(email: String, password: String): AuthUser
    suspend fun getCurrentUser(): AuthUser?
    suspend fun getNotificationToken(): String?
    suspend fun logout()
}
