package com.pascal.noctra.domain.usecase.remote.base

import com.pascal.noctra.data.repository.base.BaseRepository
import com.pascal.noctra.domain.mapper.toDomain
import com.pascal.noctra.domain.model.base.BaseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class BaseUseCaseImpl(
    private val repository: BaseRepository
) : BaseUseCase {

    override suspend fun getItems(): Flow<List<BaseItem>> = flow {
        emit(repository.getItems().data?.map { it.toDomain() }.orEmpty())
    }

    override suspend fun getItem(slug: String): Flow<BaseItem> = flow {
        emit(repository.getItem(slug).toDomain())
    }
}
