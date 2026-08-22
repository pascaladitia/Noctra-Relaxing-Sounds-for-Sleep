package com.pascal.noctra.ui.screen.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalHomeEvent = compositionLocalOf { HomeEvent() }

@Stable
data class HomeEvent(
    val onNavigateToMixer: () -> Unit = {},
    val onNavigateToSettings: () -> Unit = {}
)
