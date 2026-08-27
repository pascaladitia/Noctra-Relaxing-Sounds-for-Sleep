package com.pascal.noctra.domain.usecase.local

import com.pascal.noctra.data.local.database.AppDatabase
import com.pascal.noctra.domain.mapper.toDomain
import com.pascal.noctra.domain.mapper.toEntity
import com.pascal.noctra.domain.model.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class LocalProfileUseCaseImpl(
    private val database: AppDatabase,
) : LocalProfileUseCase {

    override suspend fun getProfileById(id: Long): Flow<Profile> = flow {
        emit(database.profileDao().getProfileById(id).toDomain())
    }

    override suspend fun getAllProfiles(): Flow<List<Profile>> = flow {
        emit(database.profileDao().getAllProfiles().map { it.toDomain() })
    }

    override suspend fun deleteProfileById(item: Profile) {
        return database.profileDao().deleteProfile(item.toEntity())
    }

    override suspend fun insertProfile(item: Profile) {
        return database.profileDao().insertProfile(item.toEntity())
    }
}