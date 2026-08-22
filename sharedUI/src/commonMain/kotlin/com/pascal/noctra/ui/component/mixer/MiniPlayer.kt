package com.pascal.noctra.ui.component.mixer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pascal.noctra.domain.model.mixer.MixerState
import com.pascal.noctra.ui.theme.NocturneAccent
import com.pascal.noctra.ui.theme.NocturneGlass
import com.pascal.noctra.ui.theme.NocturneGlassBorder
import com.pascal.noctra.ui.theme.NocturneTextMuted

@Composable
fun MiniPlayer(
    mixerState: MixerState,
    onTogglePlayPause: () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = mixerState.hasActiveSounds,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, NocturneGlassBorder, RoundedCornerShape(16.dp))
                .clickable(onClick = onExpand),
            colors = CardDefaults.cardColors(containerColor = NocturneGlass),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Noctra",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = NocturneAccent
                    )
                    Text(
                        text = "${mixerState.activeSoundCount} sound${if (mixerState.activeSoundCount != 1) "s" else ""} active",
                        style = MaterialTheme.typography.bodySmall,
                        color = NocturneTextMuted
                    )
                }

                if (mixerState.sleepTimer.isActive) {
                    Text(
                        text = mixerState.sleepTimer.displayTime,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFFFF9800),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                IconButton(
                    onClick = onTogglePlayPause,
                    modifier = Modifier
                        .size(40.dp)
                        .background(NocturneAccent, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = if (mixerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (mixerState.isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
