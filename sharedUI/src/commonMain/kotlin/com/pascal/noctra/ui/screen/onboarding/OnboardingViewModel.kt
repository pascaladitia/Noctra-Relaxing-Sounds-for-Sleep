package com.pascal.noctra.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import com.pascal.noctra.domain.usecase.local.settings.SettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.pascal.noctra.ui.screen.onboarding.state.OnboardingUiState

class OnboardingViewModel(
    private val settingsUseCase: SettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun nextPage() {
        _uiState.update { it.copy(currentPage = it.currentPage + 1) }
    }

    fun completeOnboarding() {
        settingsUseCase.setOnboardingCompleted(true)
    }
}
