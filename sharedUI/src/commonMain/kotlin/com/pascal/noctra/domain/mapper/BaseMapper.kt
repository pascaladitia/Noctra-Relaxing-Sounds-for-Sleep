package com.pascal.noctra.domain.mapper

import com.pascal.noctra.data.remote.dtos.BaseDto
import com.pascal.noctra.domain.model.base.BaseItem

fun BaseDto.toDomain(): BaseItem = BaseItem(
    title = title.orEmpty(),
    slug = slug.orEmpty(),
    image = image.orEmpty(),
    description = description.orEmpty()
)

fun List<BaseDto>.toDomain(): List<BaseItem> = map { it.toDomain() }
