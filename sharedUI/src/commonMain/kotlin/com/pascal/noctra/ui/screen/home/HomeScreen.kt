package com.pascal.noctra.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pascal.noctra.domain.model.sound.SoundCategory
import com.pascal.noctra.ui.component.home.CategoryChip
import com.pascal.noctra.ui.component.home.PresetCard
import com.pascal.noctra.ui.component.home.SoundGridCard
import com.pascal.noctra.ui.component.screenUtils.topShadow
import com.pascal.noctra.ui.screen.home.state.HomeUiState
import com.pascal.noctra.ui.screen.home.state.LocalHomeEvent
import com.pascal.noctra.ui.theme.NocturneTextMuted
import com.pascal.noctra.utils.getGreeting

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState()
) {
    val event = LocalHomeEvent.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SoundCategory.entries) { category ->
                CategoryChip(
                    category = category,
                    isSelected = uiState.selectedCategory == category,
                    onClick = { event.onCategorySelected(category) }
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
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
                .weight(1f),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSounds) { sound ->
                SoundGridCard(
                    sound = sound,
                    isActive = uiState.activeSoundIds.contains(sound.id),
                    onToggle = { event.onToggleSound(sound) }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
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
                        onPlay = { event.onPlayPreset(preset) }
                    )
                }
            }
        }
    }
}
