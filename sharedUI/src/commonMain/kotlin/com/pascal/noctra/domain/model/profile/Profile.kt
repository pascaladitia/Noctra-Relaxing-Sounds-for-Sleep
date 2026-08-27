package com.pascal.noctra.domain.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Long = 0,
    val name: String?,
    val imagePath: String?,
    val imageProfilePath: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
)