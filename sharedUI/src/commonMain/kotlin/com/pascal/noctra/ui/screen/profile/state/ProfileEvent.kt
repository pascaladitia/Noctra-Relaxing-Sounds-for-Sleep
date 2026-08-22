package com.pascal.noctra.ui.screen.profile.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalProfileEvent = compositionLocalOf { ProfileEvent() }

@Stable
data class ProfileEvent(
    val onToggleBottomSheet: () -> Unit = {},
    val onPhotoSelected: (ByteArray?, String) -> Unit = { _, _ -> },
    val onNavBack: () -> Unit = {}
)
