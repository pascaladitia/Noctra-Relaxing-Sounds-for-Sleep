package com.pascal.noctra.domain.model.sound

import kotlinx.serialization.Serializable

@Serializable
enum class SoundCategory(val displayName: String) {
    ALL("All"),
    NATURE("Nature"),
    RAIN("Rain"),
    NOISE("Noise"),
    AMBIENT("Ambient")
}
