package com.pascal.noctra.data.preferences

import com.pascal.noctra.domain.model.preset.Preset
import com.russhwolf.settings.Settings
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object PrefsPreset {
    private val settings: Settings by lazy {
        com.pascal.noctra.createSettings()
    }

    fun getCustomPresets(): List<Preset> {
        val json = settings.getString("custom_presets", "[]")
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveCustomPresets(presets: List<Preset>) {
        val json = Json.encodeToString(ListSerializer(Preset.serializer()), presets)
        settings.putString("custom_presets", json)
    }
}
