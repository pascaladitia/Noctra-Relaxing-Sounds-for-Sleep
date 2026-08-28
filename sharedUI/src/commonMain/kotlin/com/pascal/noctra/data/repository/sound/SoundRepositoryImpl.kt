package com.pascal.noctra.data.repository.sound

import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class SoundRepositoryImpl : SoundRepository {

    private val sounds = listOf(
        // Nature
        Sound("rain", "Rain", "rain", SoundCategory.NATURE, "rain", false),
        Sound("thunder", "Thunder", "thunder", SoundCategory.NATURE, "thunder", false),
        Sound("ocean", "Ocean Waves", "ocean", SoundCategory.NATURE, "ocean", false),
        Sound("river", "River Stream", "river", SoundCategory.NATURE, "river", false),
        Sound("forest", "Forest", "forest", SoundCategory.NATURE, "forest", false),
        Sound("wind", "Wind", "wind", SoundCategory.NATURE, "wind", false),
        Sound("birds", "Birds Chirping", "birds", SoundCategory.NATURE, "birds", false),
        Sound("crickets", "Crickets", "crickets", SoundCategory.NATURE, "crickets", false),
        Sound("waterfall", "Waterfall", "waterfall", SoundCategory.NATURE, "waterfall", false),
        Sound("night", "Night", "night", SoundCategory.NATURE, "night", false),
        Sound("forest_night", "Forest Night", "forest", SoundCategory.NATURE, "forest_night", false),
        Sound("wind_gentle", "Gentle Wind", "wind", SoundCategory.NATURE, "wind_gentle", false),
        Sound("thunder_distant", "Distant Thunder", "thunder", SoundCategory.NATURE, "thunder_distant", false),
        Sound("river_stream", "River Stream", "river", SoundCategory.NATURE, "river_stream", false),
        Sound("ocean_deep", "Deep Ocean", "ocean", SoundCategory.NATURE, "ocean_deep", false),
        Sound("wind_howl", "Howling Wind", "wind", SoundCategory.NATURE, "wind_howl", false),
        Sound("forest_day", "Forest Day", "forest", SoundCategory.NATURE, "forest_day", false),
        Sound("ocean_shore", "Ocean Shore", "ocean", SoundCategory.NATURE, "ocean_shore", false),
        Sound("river_deep", "Deep River", "river", SoundCategory.NATURE, "river_deep", false),
        Sound("thunder_storm", "Thunder Storm", "thunder", SoundCategory.NATURE, "thunder_storm", false),
        // Rain
        Sound("heavy_rain", "Heavy Rain", "heavy_rain", SoundCategory.RAIN, "heavy_rain", false),
        Sound("light_rain", "Light Rain", "light_rain", SoundCategory.RAIN, "light_rain", false),
        Sound("rain_window", "Rain on Window", "rain_window", SoundCategory.RAIN, "rain_window", false),
        Sound("rain_leaves", "Rain on Leaves", "rain_leaves", SoundCategory.RAIN, "rain_leaves", false),
        Sound("water_drops", "Water Drops", "rain", SoundCategory.RAIN, "water_drops", false),
        Sound("storm", "Thunderstorm", "storm", SoundCategory.RAIN, "storm", false),
        Sound("rain_heavy", "Heavy Rain", "heavy_rain", SoundCategory.RAIN, "rain_heavy", false),
        Sound("rain_forest", "Rain Forest", "rain_forest", SoundCategory.RAIN, "rain_forest", false),
        // Noise
        Sound("white_noise", "White Noise", "white_noise", SoundCategory.NOISE, "white_noise", false),
        Sound("pink_noise", "Pink Noise", "pink_noise", SoundCategory.NOISE, "pink_noise", false),
        Sound("brown_noise", "Brown Noise", "brown_noise", SoundCategory.NOISE, "brown_noise", false),
        Sound("fan", "Fan", "fan", SoundCategory.NOISE, "fan", false),
        Sound("air_conditioner", "Air Conditioner", "air_conditioner", SoundCategory.NOISE, "air_conditioner", false),
        Sound("vacuum", "Vacuum Cleaner", "vacuum", SoundCategory.NOISE, "vacuum", false),
        // Ambient
        Sound("fireplace", "Fireplace", "fireplace", SoundCategory.AMBIENT, "fireplace", false),
        Sound("cafe", "Coffee Shop", "cafe", SoundCategory.AMBIENT, "cafe", false),
        Sound("train", "Train", "train", SoundCategory.AMBIENT, "train", false),
        Sound("creek", "Creek", "creek", SoundCategory.AMBIENT, "creek", false),
        Sound("whale", "Whale Song", "whale", SoundCategory.AMBIENT, "whale", false),
        Sound("campfire", "Campfire", "campfire", SoundCategory.AMBIENT, "campfire", false),
        Sound("cave", "Cave", "cave", SoundCategory.AMBIENT, "cave", false),
        Sound("airplane", "Airplane", "airplane", SoundCategory.AMBIENT, "airplane", false),
        Sound("keyboard", "Keyboard", "keyboard", SoundCategory.AMBIENT, "keyboard", false),
        Sound("wind_chimes", "Wind Chimes", "wind_chimes", SoundCategory.AMBIENT, "wind_chimes", false),
        Sound("heartbeat", "Heartbeat", "heartbeat", SoundCategory.AMBIENT, "heartbeat", false)
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
