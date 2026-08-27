package com.pascal.noctra.ui.screen.profile

import androidx.lifecycle.ViewModel
import com.pascal.noctra.ui.screen.profile.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = !it.showBottomSheet) }
    }

    fun hideBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }
}
