package com.pascal.noctra.domain.usecase.local

import com.pascal.noctra.domain.model.profile.Profile
import kotlinx.coroutines.flow.Flow


interface LocalProfileUseCase {
    suspend fun getProfileById(id: Long): Flow<Profile>
    suspend fun getAllProfiles(): Flow<List<Profile>>
    suspend fun insertProfile(item: Profile)
    suspend fun deleteProfileById(item: Profile)
}