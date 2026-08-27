package com.pascal.noctra.ui.component.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pascal.noctra.domain.model.sound.SoundCategory
import com.pascal.noctra.ui.theme.NocturneAccent
import com.pascal.noctra.ui.theme.NocturneGlass
import com.pascal.noctra.ui.theme.NocturneGlassBorder

@Composable
fun CategoryChip(
    category: SoundCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .then(
                if (isSelected) {
                    Modifier.background(NocturneAccent.copy(alpha = 0.2f))
                        .border(1.dp, NocturneAccent, shape)
                } else {
                    Modifier.background(NocturneGlass)
                        .border(1.dp, NocturneGlassBorder, shape)
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) NocturneAccent else MaterialTheme.colorScheme.onSurface
        )
    }
}
