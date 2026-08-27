package com.pascal.noctra.domain.usecase.remote.base

import com.pascal.noctra.domain.model.base.BaseItem
import kotlinx.coroutines.flow.Flow

interface BaseUseCase {
    suspend fun getItems(): Flow<List<BaseItem>>
    suspend fun getItem(slug: String): Flow<BaseItem>
}
