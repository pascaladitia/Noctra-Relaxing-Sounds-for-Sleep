package com.pascal.noctra.domain.usecase.local.preset

import com.pascal.noctra.domain.model.preset.Preset
import kotlinx.coroutines.flow.Flow

interface PresetUseCase {
    fun getCuratedPresets(): Flow<List<Preset>>
    fun getCustomPresets(): Flow<List<Preset>>
    fun getAllPresets(): Flow<List<Preset>>
    fun saveCustomPreset(preset: Preset)
    fun deleteCustomPreset(id: String)
    fun getCuratedPresetById(id: String): Preset?
}
