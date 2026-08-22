package com.pascal.noctra.data.local.repository.profile

import com.pascal.noctra.data.local.database.AppDatabase
import com.pascal.noctra.data.local.entity.ProfileEntity
import org.koin.core.annotation.Single

@Single
class LocalProfileRepositoryImpl(
    private val database: AppDatabase,
) : LocalProfileRepository {

    override suspend fun getProfileById(id: Long): ProfileEntity? {
        return database.profileDao().getProfileById(id)
    }

    override suspend fun getAllProfiles(): List<ProfileEntity> {
        return database.profileDao().getAllProfiles()
    }

    override suspend fun deleteProfileById(item: ProfileEntity) {
        return database.profileDao().deleteProfile(item)
    }

    override suspend fun insertProfile(item: ProfileEntity) {
        return database.profileDao().insertProfile(item)
    }
}