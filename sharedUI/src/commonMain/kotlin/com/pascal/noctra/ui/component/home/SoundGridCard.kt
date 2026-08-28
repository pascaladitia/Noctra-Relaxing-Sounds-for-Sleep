package com.pascal.noctra.ui.component.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.ui.component.SoundIcon
import com.pascal.noctra.ui.theme.*

@Composable
fun SoundGridCard(
    sound: Sound,
    isActive: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = if (isActive) NocturneAccent.copy(alpha = 0.5f) else NocturneGlassBorder,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onToggle),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) NocturneAccent.copy(alpha = 0.15f) else NocturneGlass
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .shadow(
                            elevation = if (isActive) 16.dp else 0.dp,
                            shape = CircleShape,
                            ambientColor = NocturneGlow,
                            spotColor = NocturneGlow
                        )
                        .background(
                            color = if (isActive) NocturneAccent.copy(alpha = 0.2f)
                            else Color.White.copy(alpha = 0.05f),
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = if (isActive) NocturneAccent.copy(alpha = 0.3f) else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    SoundIcon(
                        iconKey = sound.icon,
                        tint = if (isActive) NocturneAccent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = sound.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = if (isActive) NocturneAccent else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            if (isActive) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .background(NocturneAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
