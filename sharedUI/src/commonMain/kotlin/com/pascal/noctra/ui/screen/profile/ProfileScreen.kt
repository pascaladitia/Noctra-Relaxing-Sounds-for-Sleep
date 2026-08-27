package com.pascal.noctra.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.noctra.ui.component.button.ButtonComponent
import com.pascal.noctra.ui.component.screenUtils.PhotoPickerSheet
import com.pascal.noctra.ui.screen.profile.state.LocalProfileEvent
import com.pascal.noctra.ui.screen.profile.state.ProfileUiState
import com.pascal.noctra.ui.theme.AppTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState = ProfileUiState()
) {
    val event = LocalProfileEvent.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Profile Template",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = "Tap the button below to open a bottom sheet.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        ButtonComponent(
            text = "Open Bottom Sheet",
            onClick = event.onToggleBottomSheet
        )
    }

    if (uiState.showBottomSheet) {
        PhotoPickerSheet(
            onPhotoSelected = event.onPhotoSelected,
            onDismiss = event.onToggleBottomSheet
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen()
    }
}
