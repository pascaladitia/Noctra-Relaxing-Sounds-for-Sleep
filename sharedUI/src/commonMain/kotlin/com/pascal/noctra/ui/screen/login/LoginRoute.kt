package com.pascal.noctra.ui.screen.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import noctra.sharedui.generated.resources.Res
import noctra.sharedui.generated.resources.close
import com.pascal.noctra.ui.component.dialog.ShowDialog
import com.pascal.noctra.ui.component.screenUtils.LoadingScreen
import com.pascal.noctra.ui.screen.login.state.LocalLoginEvent
import com.pascal.noctra.utils.AppBackHandler
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = koinInject<LoginViewModel>(),
    onLoginSuccess: () -> Unit,
    onNavBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val event = LocalLoginEvent.current

    AppBackHandler(true) {
        onNavBack()
    }

    if (uiState.isLoading) LoadingScreen()

    if (uiState.error.first) {
        ShowDialog(
            message = uiState.error.second,
            textButton = stringResource(Res.string.close)
        ) {
            viewModel.hideDialog()
        }
    }

    CompositionLocalProvider(
        LocalLoginEvent provides event.copy(
            onEmailChanged = viewModel::onEmailChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
            onTogglePassword = viewModel::togglePasswordVisibility,
            onToggleMode = viewModel::toggleMode,
            onEmailLogin = { viewModel.loginWithEmail(onLoginSuccess) }
        )
    ) {
        LoginScreen(uiState = uiState)
    }
}
