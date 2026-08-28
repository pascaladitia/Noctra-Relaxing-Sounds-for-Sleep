package com.pascal.noctra.data.audio

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned
import noctra.sharedui.generated.resources.Res
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSNumber
import platform.Foundation.create
import platform.Foundation.writeToFile

actual class SoundFileManager {

    private val cacheDir: String by lazy {
        val tempDir = NSTemporaryDirectory()
        val dir = "$tempDir/noctra_sounds"
        NSFileManager.defaultManager.createDirectoryAtPath(
            dir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null
        )
        dir
    }

    actual suspend fun getSoundData(soundId: String, fileName: String): SoundFileInfo {
        val cachedPath = "$cacheDir/${soundId}.ogg"
        if (NSFileManager.defaultManager.fileExistsAtPath(cachedPath)) {
            val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(cachedPath, error = null)
            val size = (attrs?.get("NSFileSize") as? NSNumber)?.longValue ?: 0
            if (size > 1000) {
                return SoundFileInfo(
                    soundId = soundId,
                    source = SoundSource.CACHED,
                    filePath = cachedPath
                )
            }
        }

        try {
            val bytes = Res.readBytes("files/sounds/${fileName}.ogg")
            if (bytes.isNotEmpty()) {
                val nsData = bytes.usePinned { pinned ->
                    NSData.create(
                        bytes = pinned.address,
                        length = bytes.size.toULong()
                    )
                }
                nsData?.writeToFile(cachedPath, atomically = true)
                if (NSFileManager.defaultManager.fileExistsAtPath(cachedPath)) {
                    return SoundFileInfo(
                        soundId = soundId,
                        source = SoundSource.BUNDLED,
                        filePath = cachedPath
                    )
                }
            }
        } catch (_: Exception) {
        }

        return SoundFileInfo(soundId = soundId, source = SoundSource.CACHED)
    }

    actual fun getCachedPath(soundId: String): String? {
        val path = "$cacheDir/${soundId}.ogg"
        if (NSFileManager.defaultManager.fileExistsAtPath(path)) {
            val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(path, error = null)
            val size = (attrs?.get("NSFileSize") as? NSNumber)?.longValue ?: 0
            if (size > 1000) return path
        }
        return null
    }

    actual suspend fun preloadAllSounds(soundIds: List<Pair<String, String>>) {
        soundIds.forEach { (id, name) ->
            getSoundData(id, name)
        }
    }

    actual fun clearCache() {
        NSFileManager.defaultManager.removeItemAtPath(cacheDir, error = null)
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
