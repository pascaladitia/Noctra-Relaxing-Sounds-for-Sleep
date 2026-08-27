package com.pascal.noctra.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.noctra.ui.screen.settings.state.LocalSettingsEvent
import com.pascal.noctra.ui.screen.settings.state.SettingsEvent
import org.koin.compose.koinInject

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = koinInject<SettingsViewModel>(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalSettingsEvent provides SettingsEvent(onNavigateBack = onNavigateBack)
    ) {
        SettingsScreen(
            uiState = uiState,
            onBackgroundPlaybackChange = viewModel::setBackgroundPlayback,
            onSoundQualityChange = viewModel::setSoundQuality,
            onMasterVolumeChange = viewModel::setMasterVolume
        )
    }
}
