package com.pascal.noctra.domain.model.sound

@kotlinx.serialization.Serializable
data class ActiveSound(
    val sound: Sound,
    val volume: Float = 0.5f,
    val isMuted: Boolean = false,
    val isPlaying: Boolean = true
)
