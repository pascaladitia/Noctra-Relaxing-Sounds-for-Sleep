package com.pascal.noctra.domain.usecase.remote.auth

import com.pascal.noctra.data.repository.auth.AuthRepository
import com.pascal.noctra.domain.model.auth.AuthUser

class AuthUseCaseImpl(
    private val repository: AuthRepository
) : AuthUseCase {
    override suspend fun loginWithEmail(email: String, password: String): AuthUser =
        repository.loginWithEmail(email, password)

    override suspend fun registerWithEmail(email: String, password: String): AuthUser =
        repository.registerWithEmail(email, password)

    override suspend fun getCurrentUser(): AuthUser? =
        repository.getCurrentUser()

    override suspend fun getNotificationToken(): String? =
        repository.getNotificationToken()

    override suspend fun logout() {
        repository.logout()
    }
}
