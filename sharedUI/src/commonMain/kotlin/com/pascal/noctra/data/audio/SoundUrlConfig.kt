package com.pascal.noctra.data.audio

object SoundUrlConfig {

    data class SoundUrlEntry(
        val soundId: String,
        val mixkitId: Int,
        val searchQuery: String
    )

    private val soundEntries = mapOf(
        "rain" to SoundUrlEntry("rain", 2393, "light rain loop"),
        "thunder" to SoundUrlEntry("thunder", 1296, "thunder deep rumble"),
        "ocean" to SoundUrlEntry("ocean", 1195, "close sea waves loop"),
        "river" to SoundUrlEntry("river", 2454, "river water flowing"),
        "forest" to SoundUrlEntry("forest", 1213, "forest ambience birds"),
        "wind" to SoundUrlEntry("wind", 2658, "wind blowing ambience"),
        "birds" to SoundUrlEntry("birds", 2467, "morning birds singing"),
        "crickets" to SoundUrlEntry("crickets", 39, "crickets insects night"),
        "heavy_rain" to SoundUrlEntry("heavy_rain", 2403, "heavy rain"),
        "light_rain" to SoundUrlEntry("light_rain", 1253, "light rain loop soft"),
        "rain_window" to SoundUrlEntry("rain_window", 1248, "heavy rain on car glass"),
        "rain_leaves" to SoundUrlEntry("rain_leaves", 1225, "forest rain loop"),
        "water_drops" to SoundUrlEntry("water_drops", 2399, "heavy rain drops"),
        "storm" to SoundUrlEntry("storm", 2391, "thunderstorm rain"),
        "white_noise" to SoundUrlEntry("white_noise", 1209, "underwater white noise"),
        "pink_noise" to SoundUrlEntry("pink_noise", 2135, "underwater transmitter hum"),
        "brown_noise" to SoundUrlEntry("brown_noise", 1177, "deep cinematic wind hum"),
        "fan" to SoundUrlEntry("fan", 0, "fan noise"),
        "air_conditioner" to SoundUrlEntry("air_conditioner", 0, "air conditioner hum"),
        "vacuum" to SoundUrlEntry("vacuum", 0, "vacuum cleaner"),
        "fireplace" to SoundUrlEntry("fireplace", 1330, "campfire crackles"),
        "cafe" to SoundUrlEntry("cafe", 444, "restaurant crowd ambience"),
        "train" to SoundUrlEntry("train", 1654, "distant train horn"),
        "creek" to SoundUrlEntry("creek", 2455, "water flowing river"),
        "whale" to SoundUrlEntry("whale", 1185, "sea waves with birds"),
        "campfire" to SoundUrlEntry("campfire", 1329, "campfire burning crackles")
    )

    fun getDownloadUrls(soundId: String): List<String> {
        val entry = soundEntries[soundId] ?: return emptyList()
        val urls = mutableListOf<String>()

        if (entry.mixkitId > 0) {
            urls.add("https://assets.mixkit.co/active_storage/sfx/${entry.mixkitId}/${entry.mixkitId}.wav")
        }

        return urls
    }

    fun getMixkitId(soundId: String): Int? {
        return soundEntries[soundId]?.mixkitId?.takeIf { it > 0 }
    }

    fun getSearchQuery(soundId: String): String {
        return soundEntries[soundId]?.searchQuery ?: soundId.replace("_", " ")
    }

    fun getAllSoundIds(): List<String> = soundEntries.keys.toList()
}
