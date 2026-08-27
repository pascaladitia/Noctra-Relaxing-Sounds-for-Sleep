package com.pascal.noctra.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.noctra.ui.screen.profile.state.LocalProfileEvent
import com.pascal.noctra.ui.screen.profile.state.ProfileEvent
import org.koin.compose.koinInject

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = koinInject<ProfileViewModel>(),
    onNavBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalProfileEvent provides ProfileEvent(
            onToggleBottomSheet = { viewModel.toggleBottomSheet() },
            onPhotoSelected = { _, _ -> viewModel.hideBottomSheet() },
            onNavBack = onNavBack
        )
    ) {
        ProfileScreen(uiState = uiState)
    }
}
