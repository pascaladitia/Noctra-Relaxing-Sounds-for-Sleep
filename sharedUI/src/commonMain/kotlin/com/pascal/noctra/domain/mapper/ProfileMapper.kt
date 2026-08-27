package com.pascal.noctra.domain.mapper

import com.pascal.noctra.data.local.entity.ProfileEntity
import com.pascal.noctra.domain.model.profile.Profile

fun ProfileEntity?.toDomain(): Profile = Profile(
    id = this?.id ?: 0L,
    name = this?.name.orEmpty(),
    imagePath = this?.imagePath.orEmpty(),
    imageProfilePath = this?.imageProfilePath.orEmpty(),
    email = this?.email.orEmpty(),
    phone = this?.phone.orEmpty(),
    address = this?.address.orEmpty(),
)

fun Profile?.toEntity(): ProfileEntity = ProfileEntity(
    id = this?.id ?: 0L,
    name = this?.name.orEmpty(),
    imagePath = this?.imagePath.orEmpty(),
    imageProfilePath = this?.imageProfilePath.orEmpty(),
    email = this?.email.orEmpty(),
    phone = this?.phone.orEmpty(),
    address = this?.address.orEmpty(),
)