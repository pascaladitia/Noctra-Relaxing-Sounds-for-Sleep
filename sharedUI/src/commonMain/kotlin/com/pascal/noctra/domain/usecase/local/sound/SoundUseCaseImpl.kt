package com.pascal.noctra.domain.usecase.local.sound

import com.pascal.noctra.data.repository.sound.SoundRepository
import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory
import kotlinx.coroutines.flow.Flow

class SoundUseCaseImpl(
    private val soundRepository: SoundRepository
) : SoundUseCase {
    override fun getSounds(): Flow<List<Sound>> = soundRepository.getSounds()
    override fun getSoundsByCategory(category: SoundCategory): Flow<List<Sound>> = soundRepository.getSoundsByCategory(category)
    override fun getSoundById(id: String): Sound? = soundRepository.getSoundById(id)
    override fun getAllSounds(): List<Sound> = soundRepository.getAllSounds()
}
