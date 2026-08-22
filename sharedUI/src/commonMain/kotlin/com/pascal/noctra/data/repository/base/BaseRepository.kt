package com.pascal.noctra.data.repository.base

import com.pascal.noctra.data.remote.dtos.BaseDto
import com.pascal.noctra.data.remote.dtos.BaseResponse

interface BaseRepository {
    suspend fun getItems(): BaseResponse<List<BaseDto>>
    suspend fun getItem(slug: String): BaseDto
}
