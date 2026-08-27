package com.pascal.noctra.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pascal.noctra.data.local.entity.ProfileEntity

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): ProfileEntity?

    @Query("SELECT * FROM profiles")
    suspend fun getAllProfiles(): List<ProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)
}
