package com.pascal.noctra.ui.screen.mixer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pascal.noctra.ui.component.mixer.AudioVisualizer
import com.pascal.noctra.ui.component.mixer.MiniPlayer
import com.pascal.noctra.ui.component.mixer.SleepTimerSheet
import com.pascal.noctra.ui.component.mixer.SoundCard
import com.pascal.noctra.ui.component.mixer.VolumeSlider
import com.pascal.noctra.ui.screen.mixer.state.LocalMixerEvent
import com.pascal.noctra.ui.screen.mixer.state.MixerUiState
import com.pascal.noctra.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MixerScreen(
    uiState: MixerUiState = MixerUiState(),
    onTogglePlayPause: () -> Unit = {},
    onSoundVolumeChange: (String, Float) -> Unit = { _, _ -> },
    onToggleMute: (String) -> Unit = {},
    onRemoveSound: (String) -> Unit = {},
    onMasterVolumeChange: (Float) -> Unit = {},
    onSetTimer: (Long) -> Unit = {},
    onCancelTimer: () -> Unit = {},
    onAddSound: () -> Unit = {}
) {
    val event = LocalMixerEvent.current
    var showTimerSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showTimerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTimerSheet = false },
            sheetState = sheetState,
            containerColor = NocturneSurface
        ) {
            SleepTimerSheet(
                timerState = uiState.mixerState.sleepTimer,
                onSetTimer = { duration ->
                    onSetTimer(duration)
                    showTimerSheet = false
                },
                onCancelTimer = {
                    onCancelTimer()
                    showTimerSheet = false
                }
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(48.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                    text = "Mixer",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onAddSound) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Sound",
                        tint = NocturneAccent
                    )
                }
            }
        }

        item {
            AudioVisualizer(
                isActive = uiState.mixerState.isPlaying,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VolumeSlider(
                    value = uiState.mixerState.masterVolume,
                    onValueChange = onMasterVolumeChange,
                    label = "Master",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onTogglePlayPause,
                    modifier = Modifier
                        .size(48.dp)
                        .background(NocturneAccent, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = if (uiState.mixerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { showTimerSheet = true },
                    modifier = Modifier
                        .size(48.dp)
                        .background(NocturneGlass, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Sleep Timer",
                        tint = if (uiState.mixerState.sleepTimer.isActive) NocturneTimerOrange else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Text(
                text = "Active Layers (${uiState.mixerState.activeSoundCount})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (uiState.mixerState.activeSounds.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NocturneGlass),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "\uD83C\uDFB5", style = MaterialTheme.typography.headlineLarge)
                        Text(
                            text = "No sounds playing",
                            style = MaterialTheme.typography.bodyLarge,
                            color = NocturneTextMuted
                        )
                        Text(
                            text = "Tap + to add sounds",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NocturneTextMuted
                        )
                    }
                }
            }
        }

        items(
            items = uiState.mixerState.activeSounds,
            key = { it.sound.id }
        ) { activeSound ->
            SoundCard(
                activeSound = activeSound,
                onVolumeChange = { onSoundVolumeChange(activeSound.sound.id, it) },
                onToggleMute = { onToggleMute(activeSound.sound.id) },
                onRemove = { onRemoveSound(activeSound.sound.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            MiniPlayer(
                mixerState = uiState.mixerState,
                onTogglePlayPause = onTogglePlayPause,
                onExpand = {}
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
