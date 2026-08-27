package com.pascal.noctra.domain.model.sound

import kotlinx.serialization.Serializable

@Serializable
data class Sound(
    val id: String,
    val name: String,
    val icon: String,
    val category: SoundCategory,
    val fileName: String,
    val isPremium: Boolean = false
)
