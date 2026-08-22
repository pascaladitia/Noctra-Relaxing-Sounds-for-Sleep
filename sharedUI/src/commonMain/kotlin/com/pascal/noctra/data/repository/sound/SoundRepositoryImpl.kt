package com.pascal.noctra.data.repository.sound

import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class SoundRepositoryImpl : SoundRepository {

    private val sounds = listOf(
        // Nature
        Sound("rain", "Rain", "\uD83C\uDF27\uFE0F", SoundCategory.NATURE, "rain", false),
        Sound("thunder", "Thunder", "\u26C8\uFE0F", SoundCategory.NATURE, "thunder", false),
        Sound("ocean", "Ocean Waves", "\uD83C\uDF0A", SoundCategory.NATURE, "ocean", false),
        Sound("river", "River Stream", "\uD83C\uDFDE\uFE0F", SoundCategory.NATURE, "river", false),
        Sound("forest", "Forest", "\uD83C\uDF32", SoundCategory.NATURE, "forest", false),
        Sound("wind", "Wind", "\uD83D\uDCA8", SoundCategory.NATURE, "wind", false),
        Sound("birds", "Birds Chirping", "\uD83D\uDC26", SoundCategory.NATURE, "birds", false),
        Sound("crickets", "Crickets", "\uD83D\uDC1B", SoundCategory.NATURE, "crickets", false),
        // Rain
        Sound("heavy_rain", "Heavy Rain", "\u2614\uFE0F", SoundCategory.RAIN, "heavy_rain", false),
        Sound("light_rain", "Light Rain", "\uD83C\uDF26\uFE0F", SoundCategory.RAIN, "light_rain", false),
        Sound("rain_window", "Rain on Window", "\uD83C\uDF1F", SoundCategory.RAIN, "rain_window", false),
        Sound("rain_leaves", "Rain on Leaves", "\uD83C\uDF3F", SoundCategory.RAIN, "rain_leaves", false),
        Sound("water_drops", "Water Drops", "\uD83D\uDCA7", SoundCategory.RAIN, "water_drops", false),
        Sound("storm", "Thunderstorm", "\u26C8\uFE0F", SoundCategory.RAIN, "storm", false),
        // Noise
        Sound("white_noise", "White Noise", "\u2601\uFE0F", SoundCategory.NOISE, "white_noise", false),
        Sound("pink_noise", "Pink Noise", "\uD83D\uDFE0", SoundCategory.NOISE, "pink_noise", false),
        Sound("brown_noise", "Brown Noise", "\uD83C\uDFD4\uFE0F", SoundCategory.NOISE, "brown_noise", false),
        Sound("fan", "Fan", "\uD83D\uDD2C", SoundCategory.NOISE, "fan", false),
        Sound("air_conditioner", "Air Conditioner", "\u2744\uFE0F", SoundCategory.NOISE, "ac", false),
        Sound("vacuum", "Vacuum Cleaner", "\uD83E\uDDF9", SoundCategory.NOISE, "vacuum", false),
        // Ambient
        Sound("fireplace", "Fireplace", "\uD83D\uDD25", SoundCategory.AMBIENT, "fireplace", false),
        Sound("cafe", "Coffee Shop", "\u2615", SoundCategory.AMBIENT, "cafe", false),
        Sound("train", "Train", "\uD83D\uDE82", SoundCategory.AMBIENT, "train", false),
        Sound("creek", "Creek", "\uD83C\uDFD5\uFE0F", SoundCategory.AMBIENT, "creek", false),
        Sound("whale", "Whale Song", "\uD83D\uDC0B", SoundCategory.AMBIENT, "whale", false),
        Sound("campfire", "Campfire", "\u26FA", SoundCategory.AMBIENT, "campfire", false)
    )

    private val soundsFlow = MutableStateFlow(sounds)

    override fun getSounds(): Flow<List<Sound>> = soundsFlow

    override fun getSoundsByCategory(category: SoundCategory): Flow<List<Sound>> {
        return if (category == SoundCategory.ALL) {
            soundsFlow
        } else {
            flowOf(sounds.filter { it.category == category })
        }
    }

    override fun getSoundById(id: String): Sound? = sounds.find { it.id == id }

    override fun getAllSounds(): List<Sound> = sounds
}
