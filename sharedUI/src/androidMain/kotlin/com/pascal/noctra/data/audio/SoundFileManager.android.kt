package com.pascal.noctra.data.audio

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import noctra.sharedui.generated.resources.Res
import java.io.File
import java.io.FileOutputStream

actual class SoundFileManager(private val context: Context) {

    private val cacheDir: File
        get() = File(context.cacheDir, "sounds").also { it.mkdirs() }

    actual suspend fun getSoundData(soundId: String, fileName: String): SoundFileInfo {
        return withContext(Dispatchers.IO) {
            val cached = File(cacheDir, "${soundId}.ogg")
            if (cached.exists() && cached.length() > 1000) {
                return@withContext SoundFileInfo(
                    soundId = soundId,
                    source = SoundSource.CACHED,
                    filePath = cached.absolutePath
                )
            }

            try {
                val bytes = Res.readBytes("files/sounds/${fileName}.ogg")
                if (bytes.isNotEmpty()) {
                    FileOutputStream(cached).use { it.write(bytes) }
                    if (cached.exists() && cached.length() > 1000) {
                        return@withContext SoundFileInfo(
                            soundId = soundId,
                            source = SoundSource.BUNDLED,
                            filePath = cached.absolutePath
                        )
                    }
                }
            } catch (_: Exception) {
            }

            SoundFileInfo(soundId = soundId, source = SoundSource.CACHED)
        }
    }

    actual fun getCachedPath(soundId: String): String? {
        val cached = File(cacheDir, "${soundId}.ogg")
        return if (cached.exists() && cached.length() > 1000) cached.absolutePath else null
    }

    actual suspend fun preloadAllSounds(soundIds: List<Pair<String, String>>) {
        withContext(Dispatchers.IO) {
            soundIds.forEach { (id, name) ->
                getSoundData(id, name)
            }
        }
    }

    actual fun clearCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }

    actual suspend fun hasBundledSound(soundId: String): Boolean {
        return try {
            val bytes = Res.readBytes("files/sounds/${soundId}.ogg")
            bytes.isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }
}
