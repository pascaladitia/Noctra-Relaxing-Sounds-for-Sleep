package com.pascal.noctra.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pascal.noctra.ui.screen.settings.state.LocalSettingsEvent
import com.pascal.noctra.ui.screen.settings.state.SettingsUiState
import com.pascal.noctra.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState = SettingsUiState(),
    onBackgroundPlaybackChange: (Boolean) -> Unit = {},
    onSoundQualityChange: (String) -> Unit = {},
    onMasterVolumeChange: (Float) -> Unit = {}
) {
    val event = LocalSettingsEvent.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = event.onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsSection("Playback") {
            SettingsToggleItem(
                title = "Background Playback",
                subtitle = "Keep audio playing when app is in background",
                checked = uiState.backgroundPlayback,
                onCheckedChange = onBackgroundPlaybackChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Sound Quality",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("high", "standard").forEach { quality ->
                        FilterChip(
                            selected = uiState.soundQuality == quality,
                            onClick = { onSoundQualityChange(quality) },
                            label = { Text(quality.replaceFirstChar { it.uppercase() }) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NocturneAccent.copy(alpha = 0.2f),
                                selectedLabelColor = NocturneAccent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Default Master Volume",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${(uiState.masterVolume * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NocturneTextMuted
                    )
                }
                Slider(
                    value = uiState.masterVolume,
                    onValueChange = onMasterVolumeChange,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = NocturneAccent,
                        activeTrackColor = NocturneAccent,
                        inactiveTrackColor = NocturneTextMuted.copy(alpha = 0.2f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection("About") {
            SettingsTextItem(title = "App Version", subtitle = "1.0.0")
            SettingsTextItem(title = "Privacy Policy", subtitle = "View our privacy policy")
            SettingsTextItem(title = "Terms of Service", subtitle = "View terms")
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NocturneAccent,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NocturneGlass),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = NocturneTextMuted
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NocturneAccent,
                checkedTrackColor = NocturneAccent.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
private fun SettingsTextItem(
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = NocturneTextMuted)
        }
    }
}
