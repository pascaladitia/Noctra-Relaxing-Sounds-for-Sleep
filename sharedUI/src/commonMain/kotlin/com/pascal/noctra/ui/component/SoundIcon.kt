package com.pascal.noctra.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

fun SoundIconVector(iconKey: String): ImageVector {
    return when (iconKey) {
        "rain", "heavy_rain", "light_rain", "rain_window", "rain_leaves", "rain_heavy", "rain_forest" ->
            Icons.Filled.Umbrella
        "thunder", "thunder_distant", "thunder_storm", "storm" ->
            Icons.Filled.FlashOn
        "ocean", "ocean_deep", "ocean_shore" ->
            Icons.Filled.Waves
        "river", "river_stream", "river_deep", "creek", "waterfall", "water_drops" ->
            Icons.Filled.WaterDrop
        "forest", "forest_night", "forest_day" ->
            Icons.Filled.Park
        "wind", "wind_gentle", "wind_howl" ->
            Icons.Filled.Air
        "birds" ->
            Icons.Filled.Pets
        "crickets" ->
            Icons.Filled.PestControl
        "night" ->
            Icons.Filled.DarkMode
        "white_noise", "pink_noise", "brown_noise" ->
            Icons.Filled.GraphicEq
        "fan" ->
            Icons.Filled.WindPower
        "air_conditioner" ->
            Icons.Filled.AcUnit
        "vacuum" ->
            Icons.Filled.CleaningServices
        "fireplace", "campfire" ->
            Icons.Filled.LocalFireDepartment
        "cafe" ->
            Icons.Filled.LocalCafe
        "train" ->
            Icons.Filled.Train
        "whale" ->
            Icons.Filled.Spa
        "airplane" ->
            Icons.Filled.Flight
        "keyboard" ->
            Icons.Filled.Keyboard
        "wind_chimes" ->
            Icons.Filled.MusicNote
        "heartbeat" ->
            Icons.Filled.Favorite
        else ->
            Icons.Filled.MusicNote
    }
}

@Composable
fun SoundIcon(
    iconKey: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Icon(
        imageVector = SoundIconVector(iconKey),
        contentDescription = null,
        modifier = modifier,
        tint = tint
    )
}
