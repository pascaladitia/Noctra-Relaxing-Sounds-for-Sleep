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
            icon = "\uD83C\uDF19",
            sounds = listOf(
                ActiveSound(Sound("rain", "Rain", "\uD83C\uDF27\uFE0F", SoundCategory.NATURE, "rain"), volume = 0.6f),
                ActiveSound(Sound("brown_noise", "Brown Noise", "\uD83C\uDFD4\uFE0F", SoundCategory.NOISE, "brown_noise"), volume = 0.4f)
            )
        ),
        Preset(
            id = "cozy_fireplace",
            name = "Cozy Fireplace",
            description = "Warm crackling fire with gentle rain outside",
            icon = "\uD83D\uDD25",
            sounds = listOf(
                ActiveSound(Sound("fireplace", "Fireplace", "\uD83D\uDD25", SoundCategory.AMBIENT, "fireplace"), volume = 0.7f),
                ActiveSound(Sound("light_rain", "Light Rain", "\uD83C\uDF26\uFE0F", SoundCategory.RAIN, "light_rain"), volume = 0.3f)
            )
        ),
        Preset(
            id = "ocean_dreams",
            name = "Ocean Dreams",
            description = "Soothing ocean waves with seagulls in the distance",
            icon = "\uD83C\uDF0A",
            sounds = listOf(
                ActiveSound(Sound("ocean", "Ocean Waves", "\uD83C\uDF0A", SoundCategory.NATURE, "ocean"), volume = 0.7f),
                ActiveSound(Sound("birds", "Birds Chirping", "\uD83D\uDC26", SoundCategory.NATURE, "birds"), volume = 0.2f)
            )
        ),
        Preset(
            id = "forest_night",
            name = "Forest Night",
            description = "Nighttime forest ambiance with crickets and wind",
            icon = "\uD83C\uDF32",
            sounds = listOf(
                ActiveSound(Sound("forest", "Forest", "\uD83C\uDF32", SoundCategory.NATURE, "forest"), volume = 0.5f),
                ActiveSound(Sound("crickets", "Crickets", "\uD83D\uDC1B", SoundCategory.NATURE, "crickets"), volume = 0.4f),
                ActiveSound(Sound("wind", "Wind", "\uD83D\uDCA8", SoundCategory.NATURE, "wind"), volume = 0.3f)
            )
        ),
        Preset(
            id = "rainy_cabin",
            name = "Rainy Cabin",
            description = "Heavy rain on a cabin roof with thunder in the distance",
            icon = "\u2614\uFE0F",
            sounds = listOf(
                ActiveSound(Sound("heavy_rain", "Heavy Rain", "\u2614\uFE0F", SoundCategory.RAIN, "heavy_rain"), volume = 0.7f),
                ActiveSound(Sound("thunder", "Thunder", "\u26C8\uFE0F", SoundCategory.NATURE, "thunder"), volume = 0.3f)
            )
        ),
        Preset(
            id = "underwater",
            name = "Underwater",
            description = "Deep ocean ambiance with whale songs",
            icon = "\uD83D\uDC0B",
            sounds = listOf(
                ActiveSound(Sound("whale", "Whale Song", "\uD83D\uDC0B", SoundCategory.AMBIENT, "whale"), volume = 0.6f),
                ActiveSound(Sound("water_drops", "Water Drops", "\uD83D\uDCA7", SoundCategory.RAIN, "water_drops"), volume = 0.3f)
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
