package com.pascal.noctra.domain.model.preset

import com.pascal.noctra.domain.model.sound.ActiveSound
import kotlinx.serialization.Serializable

@Serializable
data class Preset(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val sounds: List<ActiveSound>,
    val isCustom: Boolean = false
)
