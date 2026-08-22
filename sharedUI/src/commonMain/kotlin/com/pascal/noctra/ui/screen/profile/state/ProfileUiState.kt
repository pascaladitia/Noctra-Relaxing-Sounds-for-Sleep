package com.pascal.noctra.ui.screen.profile.state

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: Pair<Boolean, String> = false to "",
    val showBottomSheet: Boolean = false
)
