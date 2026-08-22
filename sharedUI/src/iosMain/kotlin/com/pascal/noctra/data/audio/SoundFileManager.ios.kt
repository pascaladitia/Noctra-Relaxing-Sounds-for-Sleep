package com.pascal.noctra.data.audio

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import platform.posix.memcpy

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
        val cachedPath = "$cacheDir/${soundId}.wav"
        if (NSFileManager.defaultManager.fileExistsAtPath(cachedPath)) {
            val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(cachedPath, error = null)
            val size = (attrs?.get("NSFileSize") as? NSNumber)?.longValue ?: 0
            if (size > 1000) {
                return SoundFileInfo(
                    soundId = soundId,
                    source = SoundSource.DOWNLOADED,
                    filePath = cachedPath
                )
            }
        }

        val urls = SoundUrlConfig.getDownloadUrls(soundId)
        for (urlStr in urls) {
            try {
                val downloaded = downloadFile(urlStr, "$cacheDir/${soundId}.wav")
                if (downloaded != null) {
                    return SoundFileInfo(
                        soundId = soundId,
                        source = SoundSource.DOWNLOADED,
                        filePath = downloaded
                    )
                }
            } catch (_: Exception) {
                continue
            }
        }

        return SoundFileInfo(soundId = soundId, source = SoundSource.GENERATED)
    }

    actual fun getCachedPath(soundId: String): String? {
        val path = "$cacheDir/${soundId}.wav"
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

    private fun downloadFile(urlStr: String, outputPath: String): String? {
        val url = NSURL(string = urlStr)
        val data = NSData.dataWithContentsOfURL(url, options = 0, error = null) ?: return null
        if (data.length.toLong() > 1000) {
            data.writeToFile(outputPath, atomically = true)
            return outputPath
        }
        return null
    }
}
