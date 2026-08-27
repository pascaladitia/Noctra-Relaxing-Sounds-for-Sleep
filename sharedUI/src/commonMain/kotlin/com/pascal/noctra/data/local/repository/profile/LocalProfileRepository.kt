package com.pascal.noctra.data.local.repository.profile

import com.pascal.noctra.data.local.entity.ProfileEntity


interface LocalProfileRepository {
    suspend fun getProfileById(id: Long): ProfileEntity?
    suspend fun getAllProfiles(): List<ProfileEntity>
    suspend fun insertProfile(item: ProfileEntity)
    suspend fun deleteProfileById(item: ProfileEntity)
}