package com.pascal.noctra.ui.component.mixer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.VolumeOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pascal.noctra.domain.model.sound.ActiveSound
import com.pascal.noctra.ui.theme.NocturneAccent
import com.pascal.noctra.ui.theme.NocturneGlass
import com.pascal.noctra.ui.theme.NocturneGlassBorder
import com.pascal.noctra.ui.theme.NocturneTextMuted

@Composable
fun SoundCard(
    activeSound: ActiveSound,
    onVolumeChange: (Float) -> Unit,
    onToggleMute: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentAlpha = if (activeSound.isMuted) 0.5f else 1f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, NocturneGlassBorder, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = NocturneGlass),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = activeSound.sound.icon, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = activeSound.sound.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onToggleMute, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (activeSound.isMuted) Icons.Outlined.VolumeOff else Icons.Filled.VolumeUp,
                            contentDescription = if (activeSound.isMuted) "Unmute" else "Mute",
                            tint = if (activeSound.isMuted) NocturneTextMuted else NocturneAccent
                        )
                    }
                    IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color(0xFFEF5350).copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Slider(
                value = activeSound.volume,
                onValueChange = onVolumeChange,
                modifier = Modifier.fillMaxWidth().height(24.dp),
                enabled = !activeSound.isMuted,
                colors = SliderDefaults.colors(
                    thumbColor = NocturneAccent,
                    activeTrackColor = NocturneAccent,
                    inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                )
            )

            Text(
                text = "${(activeSound.volume * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = NocturneTextMuted
            )
        }
    }
}
