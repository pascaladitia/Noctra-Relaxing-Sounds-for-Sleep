package com.pascal.noctra.ui.component.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pascal.noctra.domain.model.preset.Preset
import com.pascal.noctra.ui.theme.NocturneGlass
import com.pascal.noctra.ui.theme.NocturneGlassBorder
import com.pascal.noctra.ui.theme.NocturneTextMuted

@Composable
fun PresetCard(
    preset: Preset,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, NocturneGlassBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onPlay),
        colors = CardDefaults.cardColors(containerColor = NocturneGlass),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = preset.icon, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = preset.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${preset.sounds.size} sounds",
                style = MaterialTheme.typography.bodySmall,
                color = NocturneTextMuted
            )
        }
    }
}
