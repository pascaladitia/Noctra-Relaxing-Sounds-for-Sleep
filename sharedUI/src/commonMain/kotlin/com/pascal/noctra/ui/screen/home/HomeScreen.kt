package com.pascal.noctra.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pascal.noctra.domain.model.preset.Preset
import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory
import com.pascal.noctra.ui.component.home.CategoryChip
import com.pascal.noctra.ui.component.home.PresetCard
import com.pascal.noctra.ui.component.home.SoundGridCard
import com.pascal.noctra.ui.screen.home.state.LocalHomeEvent
import com.pascal.noctra.ui.screen.home.state.HomeUiState
import com.pascal.noctra.ui.theme.NocturneTextMuted
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState(),
    onToggleSound: (Sound) -> Unit = {},
    onPlayPreset: (Preset) -> Unit = {},
    onCategorySelected: (SoundCategory) -> Unit = {}
) {
    val event = LocalHomeEvent.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = getGreeting(),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "What sounds help you sleep?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = NocturneTextMuted
                )
            }

            IconButton(onClick = event.onNavigateToSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SoundCategory.entries) { category ->
                CategoryChip(
                    category = category,
                    isSelected = uiState.selectedCategory == category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val filteredSounds = if (uiState.selectedCategory == SoundCategory.ALL) {
            uiState.sounds
        } else {
            uiState.sounds.filter { it.category == uiState.selectedCategory }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.wrapContentHeight().weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSounds) { sound ->
                SoundGridCard(
                    sound = sound,
                    isActive = uiState.activeSoundIds.contains(sound.id),
                    onToggle = { onToggleSound(sound) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Curated Presets",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.presets) { preset ->
                PresetCard(
                    preset = preset,
                    onPlay = { onPlayPreset(preset) }
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

private fun getGreeting(): String {
    val now = kotlin.time.Clock.System.now()
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = localDateTime.hour
    return when {
        hour in 5..11 -> "Good Morning"
        hour in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }
}
