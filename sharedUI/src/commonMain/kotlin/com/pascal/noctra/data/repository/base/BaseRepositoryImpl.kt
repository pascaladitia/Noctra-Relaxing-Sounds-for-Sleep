package com.pascal.noctra.data.repository.base

import com.pascal.noctra.data.remote.api.BaseApi
import com.pascal.noctra.data.remote.dtos.BaseDto
import com.pascal.noctra.data.remote.dtos.BaseResponse
import com.pascal.noctra.utils.base.SafeApiCall
import org.koin.core.annotation.Single

@Single
class BaseRepositoryImpl(
    private val api: BaseApi
) : BaseRepository, SafeApiCall() {

    override suspend fun getItems(): BaseResponse<List<BaseDto>> =
        safeApiCall { api.getItems() }

    override suspend fun getItem(slug: String): BaseDto =
        safeApiCall { api.getItem(slug) }
}
