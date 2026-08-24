package com.pascal.noctra.data.audio

enum class SoundSource {
    BUNDLED,
    CACHED
}

data class SoundFileInfo(
    val soundId: String,
    val source: SoundSource,
    val filePath: String? = null,
    val fileData: ByteArray? = null
)

expect class SoundFileManager {
    suspend fun getSoundData(soundId: String, fileName: String): SoundFileInfo
    fun getCachedPath(soundId: String): String?
    suspend fun preloadAllSounds(soundIds: List<Pair<String, String>>)
    fun clearCache()
    suspend fun hasBundledSound(soundId: String): Boolean
}
