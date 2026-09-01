package com.pascal.noctra.data.repository.preset

import com.pascal.noctra.data.preferences.PrefsPreset
import com.pascal.noctra.domain.model.preset.Preset
import com.pascal.noctra.domain.model.sound.ActiveSound
import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class PresetRepositoryImpl : PresetRepository {

    private val curatedPresets = listOf(
        Preset(
            id = "deep_sleep",
            name = "Deep Sleep",
            description = "Gentle rain with deep brown noise for maximum relaxation",
            icon = "night",
            sounds = listOf(
                ActiveSound(Sound("rain", "Rain", "rain", SoundCategory.NATURE, "rain"), volume = 0.6f),
                ActiveSound(Sound("brown_noise", "Brown Noise", "brown_noise", SoundCategory.NOISE, "brown_noise"), volume = 0.4f)
            )
        ),
        Preset(
            id = "cozy_fireplace",
            name = "Cozy Fireplace",
            description = "Warm crackling fire with gentle rain outside",
            icon = "fireplace",
            sounds = listOf(
                ActiveSound(Sound("fireplace", "Fireplace", "fireplace", SoundCategory.AMBIENT, "fireplace"), volume = 0.7f),
                ActiveSound(Sound("light_rain", "Light Rain", "light_rain", SoundCategory.RAIN, "light_rain"), volume = 0.3f)
            )
        ),
        Preset(
            id = "ocean_dreams",
            name = "Ocean Dreams",
            description = "Soothing ocean waves with seagulls in the distance",
            icon = "ocean",
            sounds = listOf(
                ActiveSound(Sound("ocean", "Ocean Waves", "ocean", SoundCategory.NATURE, "ocean"), volume = 0.7f),
                ActiveSound(Sound("birds", "Birds Chirping", "birds", SoundCategory.NATURE, "birds"), volume = 0.2f)
            )
        ),
        Preset(
            id = "forest_night",
            name = "Forest Night",
            description = "Nighttime forest ambiance with crickets and wind",
            icon = "forest",
            sounds = listOf(
                ActiveSound(Sound("forest", "Forest", "forest", SoundCategory.NATURE, "forest"), volume = 0.5f),
                ActiveSound(Sound("crickets", "Crickets", "crickets", SoundCategory.NATURE, "crickets"), volume = 0.4f),
                ActiveSound(Sound("wind", "Wind", "wind", SoundCategory.NATURE, "wind"), volume = 0.3f)
            )
        ),
        Preset(
            id = "rainy_cabin",
            name = "Rainy Cabin",
            description = "Heavy rain on a cabin roof with thunder in the distance",
            icon = "heavy_rain",
            sounds = listOf(
                ActiveSound(Sound("heavy_rain", "Heavy Rain", "heavy_rain", SoundCategory.RAIN, "heavy_rain"), volume = 0.7f),
                ActiveSound(Sound("thunder", "Thunder", "thunder", SoundCategory.NATURE, "thunder"), volume = 0.3f)
            )
        ),
        Preset(
            id = "underwater",
            name = "Underwater",
            description = "Deep ocean ambiance with whale songs",
            icon = "whale",
            sounds = listOf(
                ActiveSound(Sound("whale", "Whale Song", "whale", SoundCategory.AMBIENT, "whale"), volume = 0.6f),
                ActiveSound(Sound("water_drops", "Water Drops", "rain", SoundCategory.RAIN, "water_drops"), volume = 0.3f)
            )
        )
    )

    private val customPresets = MutableStateFlow<List<Preset>>(PrefsPreset.getCustomPresets())

    override fun getCuratedPresets(): Flow<List<Preset>> = MutableStateFlow(curatedPresets)

    override fun getCustomPresets(): Flow<List<Preset>> = customPresets

    override fun getAllPresets(): Flow<List<Preset>> = customPresets.map { custom ->
        curatedPresets + custom
    }

    override fun saveCustomPreset(preset: Preset) {
        val updated = customPresets.value + preset
        customPresets.value = updated
        PrefsPreset.saveCustomPresets(updated)
    }

    override fun deleteCustomPreset(id: String) {
        val updated = customPresets.value.filter { it.id != id }
        customPresets.value = updated
        PrefsPreset.saveCustomPresets(updated)
    }

    override fun getCuratedPresetById(id: String): Preset? = curatedPresets.find { it.id == id }
}
