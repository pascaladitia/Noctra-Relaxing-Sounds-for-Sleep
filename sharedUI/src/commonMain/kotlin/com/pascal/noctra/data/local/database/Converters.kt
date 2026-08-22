package com.pascal.noctra.data.local.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(listString: String?): List<String>? {
        return listString?.let { json.decodeFromString(it) } ?: emptyList()
    }
}
