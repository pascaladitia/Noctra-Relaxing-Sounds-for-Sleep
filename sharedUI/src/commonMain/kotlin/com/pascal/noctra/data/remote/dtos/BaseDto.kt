package com.pascal.noctra.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class BaseDto(
    val title: String? = null,
    val slug: String? = null,
    val image: String? = null,
    val description: String? = null
)
