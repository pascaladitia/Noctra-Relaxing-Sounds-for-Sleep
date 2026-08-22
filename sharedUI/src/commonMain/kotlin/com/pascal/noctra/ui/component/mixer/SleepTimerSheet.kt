package com.pascal.noctra.ui.component.mixer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pascal.noctra.domain.model.timer.SleepTimerState
import com.pascal.noctra.ui.theme.NocturneAccent
import com.pascal.noctra.ui.theme.NocturneGlass
import com.pascal.noctra.ui.theme.NocturneGlassBorder
import com.pascal.noctra.ui.theme.NocturneTimerOrange

data class TimerPreset(val label: String, val durationMs: Long)

val timerPresets = listOf(
    TimerPreset("15 min", 15 * 60 * 1000L),
    TimerPreset("30 min", 30 * 60 * 1000L),
    TimerPreset("45 min", 45 * 60 * 1000L),
    TimerPreset("1 hr", 60 * 60 * 1000L),
    TimerPreset("2 hr", 2 * 60 * 60 * 1000L)
)

@Composable
fun SleepTimerSheet(
    timerState: SleepTimerState,
    onSetTimer: (Long) -> Unit,
    onCancelTimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Sleep Timer",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (timerState.isActive) {
            Card(
                colors = CardDefaults.cardColors(containerColor = NocturneGlass),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Timer Active",
                            style = MaterialTheme.typography.titleMedium,
                            color = NocturneTimerOrange
                        )
                        Text(
                            text = timerState.displayTime,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = NocturneTimerOrange
                        )
                    }
                    TextButton(onClick = onCancelTimer) {
                        Text("Cancel", color = Color(0xFFEF5350))
                    }
                }
            }
        } else {
            timerPresets.forEach { preset ->
                Button(
                    onClick = { onSetTimer(preset.durationMs) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NocturneGlass),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = preset.label,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
