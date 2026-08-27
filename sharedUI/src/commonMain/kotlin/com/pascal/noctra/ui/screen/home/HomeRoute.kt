package com.pascal.noctra.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.noctra.ui.screen.home.state.HomeEvent
import com.pascal.noctra.ui.screen.home.state.LocalHomeEvent
import org.koin.compose.koinInject

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = koinInject<HomeViewModel>(),
    onNavigateToMixer: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalHomeEvent provides HomeEvent(
            onNavigateToMixer = onNavigateToMixer,
            onNavigateToSettings = onNavigateToSettings,
            onToggleSound = viewModel::toggleSound,
            onPlayPreset = viewModel::playPreset,
            onCategorySelected = viewModel::setCategory
        )
    ) {
        HomeScreen(uiState = uiState)
    }
}
