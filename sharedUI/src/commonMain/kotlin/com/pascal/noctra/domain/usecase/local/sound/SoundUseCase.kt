package com.pascal.noctra.domain.usecase.local.sound

import com.pascal.noctra.data.repository.sound.SoundRepository
import com.pascal.noctra.domain.model.sound.Sound
import com.pascal.noctra.domain.model.sound.SoundCategory
import kotlinx.coroutines.flow.Flow

interface SoundUseCase {
    fun getSounds(): Flow<List<Sound>>
    fun getSoundsByCategory(category: SoundCategory): Flow<List<Sound>>
    fun getSoundById(id: String): Sound?
    fun getAllSounds(): List<Sound>
}
