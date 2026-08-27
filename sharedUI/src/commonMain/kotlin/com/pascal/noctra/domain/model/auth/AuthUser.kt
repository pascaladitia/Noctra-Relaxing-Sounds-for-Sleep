package com.pascal.noctra.domain.model.auth

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val fcmToken: String?
)
