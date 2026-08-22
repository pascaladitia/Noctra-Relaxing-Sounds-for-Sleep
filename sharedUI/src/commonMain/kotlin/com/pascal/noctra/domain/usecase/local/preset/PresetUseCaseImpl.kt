package com.pascal.noctra.domain.usecase.local.preset

import com.pascal.noctra.data.repository.preset.PresetRepository
import com.pascal.noctra.domain.model.preset.Preset
import kotlinx.coroutines.flow.Flow

class PresetUseCaseImpl(
    private val presetRepository: PresetRepository
) : PresetUseCase {
    override fun getCuratedPresets(): Flow<List<Preset>> = presetRepository.getCuratedPresets()
    override fun getCustomPresets(): Flow<List<Preset>> = presetRepository.getCustomPresets()
    override fun getAllPresets(): Flow<List<Preset>> = presetRepository.getAllPresets()
    override fun saveCustomPreset(preset: Preset) = presetRepository.saveCustomPreset(preset)
    override fun deleteCustomPreset(id: String) = presetRepository.deleteCustomPreset(id)
    override fun getCuratedPresetById(id: String): Preset? = presetRepository.getCuratedPresetById(id)
}
