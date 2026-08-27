package com.pascal.noctra.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val status: String? = null,
    val creator: String? = null,
    val source: String? = null,
    val data: T? = null,
    val pagination: Pagination? = null
)

@Serializable
data class Pagination(
    val current_page: Int? = null,
    val has_next: Boolean? = null,
    val last_visible_page: Int? = null
)
