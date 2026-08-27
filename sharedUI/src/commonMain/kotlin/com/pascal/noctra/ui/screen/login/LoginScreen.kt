package com.pascal.noctra.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import noctra.sharedui.generated.resources.Res
import noctra.sharedui.generated.resources.hint_input_email
import noctra.sharedui.generated.resources.hint_input_password
import noctra.sharedui.generated.resources.label_already_have_account
import noctra.sharedui.generated.resources.label_create_account
import noctra.sharedui.generated.resources.label_email
import noctra.sharedui.generated.resources.label_fcm_ready
import noctra.sharedui.generated.resources.label_login
import noctra.sharedui.generated.resources.label_login_subtitle
import noctra.sharedui.generated.resources.label_login_title
import noctra.sharedui.generated.resources.label_new_to_aniqu
import noctra.sharedui.generated.resources.label_password
import noctra.sharedui.generated.resources.label_register
import noctra.sharedui.generated.resources.label_register_subtitle
import noctra.sharedui.generated.resources.label_register_title
import noctra.sharedui.generated.resources.logo
import com.pascal.noctra.ui.component.button.ButtonComponent
import com.pascal.noctra.ui.component.form.FormEmailComponent
import com.pascal.noctra.ui.component.form.FormPasswordComponent
import com.pascal.noctra.ui.component.screenUtils.SlideDirection
import com.pascal.noctra.ui.component.screenUtils.StaggeredAnimatedItem
import com.pascal.noctra.ui.component.screenUtils.StaggeredScope
import com.pascal.noctra.ui.screen.login.state.LocalLoginEvent
import com.pascal.noctra.ui.screen.login.state.LoginUiState
import com.pascal.noctra.ui.theme.AppTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val LoginModeSwapDelayMs = 720

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState = LoginUiState()
) {
    val event = LocalLoginEvent.current
    var displayedRegisterMode by remember { mutableStateOf(uiState.isRegisterMode) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isRegisterMode) {
        if (displayedRegisterMode != uiState.isRegisterMode) {
            contentVisible = false
            delay(LoginModeSwapDelayMs.toLong())
            displayedRegisterMode = uiState.isRegisterMode
            delay(16)
            contentVisible = true
        } else {
            delay(20)
            contentVisible = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.28f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val title = if (uiState.isRegisterMode) {
                stringResource(Res.string.label_register_title)
            } else {
                stringResource(Res.string.label_login_title)
            }
            val subtitle = if (uiState.isRegisterMode) {
                stringResource(Res.string.label_register_subtitle)
            } else {
                stringResource(Res.string.label_login_subtitle)
            }

            StaggeredScope(
                visible = contentVisible,
                totalItems = 8,
                slideDirection = if (displayedRegisterMode == uiState.isRegisterMode)
                    SlideDirection.RIGHT else SlideDirection.LEFT,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StaggeredAnimatedItem(index = 0) {
                        Image(
                            modifier = Modifier.width(112.dp),
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                    }

                    StaggeredAnimatedItem(index = 1) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    StaggeredAnimatedItem(index = 2) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    StaggeredAnimatedItem(index = 3) {
                        FormEmailComponent(
                            title = stringResource(Res.string.label_email),
                            hintText = stringResource(Res.string.hint_input_email),
                            value = uiState.email,
                            onValueChange = event.onEmailChanged,
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    StaggeredAnimatedItem(index = 4) {
                        FormPasswordComponent(
                            title = stringResource(Res.string.label_password),
                            hintText = stringResource(Res.string.hint_input_password),
                            value = uiState.password,
                            onValueChange = event.onPasswordChanged,
                            isPasswordVisible = uiState.isPasswordVisible,
                            onIconClick = event.onTogglePassword,
                            onEnter = {},
                            isError = false
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    StaggeredAnimatedItem(index = 5) {
                        ButtonComponent(
                            text = if (uiState.isRegisterMode) {
                                stringResource(Res.string.label_create_account)
                            } else {
                                stringResource(Res.string.label_login)
                            },
                            height = 54.dp,
                            enabled = !uiState.isLoading && contentVisible,
                            onClick = event.onEmailLogin
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    StaggeredAnimatedItem(index = 6) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (uiState.isRegisterMode) {
                                    stringResource(Res.string.label_already_have_account)
                                } else {
                                    stringResource(Res.string.label_new_to_aniqu)
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            TextButton(
                                enabled = !uiState.isLoading && contentVisible,
                                onClick = event.onToggleMode
                            ) {
                                Text(
                                    if (uiState.isRegisterMode) {
                                        stringResource(Res.string.label_login)
                                    } else {
                                        stringResource(Res.string.label_register)
                                    }
                                )
                            }
                        }
                    }

                    uiState.fcmToken?.takeIf { it.isNotBlank() }?.let {
                        StaggeredAnimatedItem(index = 7) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(Res.string.label_fcm_ready),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen()
    }
}
