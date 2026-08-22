package com.pascal.noctra.ui.screen.mixer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.noctra.ui.screen.mixer.state.LocalMixerEvent
import com.pascal.noctra.ui.screen.mixer.state.MixerEvent
import org.koin.compose.koinInject

@Composable
fun MixerRoute(
    viewModel: MixerViewModel = koinInject<MixerViewModel>(),
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalMixerEvent provides MixerEvent(
            onNavigateBack = onNavigateBack,
            onNavigateToHome = onNavigateToHome
        )
    ) {
        MixerScreen(
            uiState = uiState,
            onTogglePlayPause = viewModel::togglePlayPause,
            onSoundVolumeChange = viewModel::updateSoundVolume,
            onToggleMute = viewModel::toggleMute,
            onRemoveSound = viewModel::removeSound,
            onMasterVolumeChange = viewModel::setMasterVolume,
            onSetTimer = viewModel::startSleepTimer,
            onCancelTimer = viewModel::cancelSleepTimer,
            onAddSound = onNavigateToHome
        )
    }
}
