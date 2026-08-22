package com.pascal.noctra.data.remote.api

import com.pascal.noctra.data.remote.dtos.BaseDto
import com.pascal.noctra.data.remote.dtos.BaseResponse
import com.pascal.noctra.utils.base.SafeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single

@Single
class BaseApi(
    private val client: HttpClient
) : SafeApiCall() {

    suspend fun getItems(): BaseResponse<List<BaseDto>> {
        return client.get("") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getItem(slug: String): BaseDto {
        return client.post("") {
            contentType(ContentType.Application.Json)
            setBody(slug)
        }.body()
    }
}
