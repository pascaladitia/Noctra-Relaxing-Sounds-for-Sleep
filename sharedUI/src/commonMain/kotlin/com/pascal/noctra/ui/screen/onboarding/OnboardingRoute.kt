package com.pascal.noctra.ui.screen.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.noctra.ui.screen.onboarding.state.OnboardingEvent
import com.pascal.noctra.ui.screen.onboarding.state.LocalOnboardingEvent
import org.koin.compose.koinInject

@Composable
fun OnboardingRoute(
    viewModel: OnboardingViewModel = koinInject<OnboardingViewModel>(),
    onComplete: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalOnboardingEvent provides OnboardingEvent(onComplete = {
            viewModel.completeOnboarding()
            onComplete()
        })
    ) {
        OnboardingScreen(uiState = uiState)
    }
}
