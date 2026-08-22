package com.pascal.noctra.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "profiles")
@Serializable
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val imagePath: String?,
    val imageProfilePath: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
)