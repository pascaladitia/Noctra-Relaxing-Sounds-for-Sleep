package com.pascal.noctra.data.audio

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

actual class SoundFileManager(private val context: Context) {

    private val cacheDir: File
        get() = File(context.cacheDir, "sounds").also { it.mkdirs() }

    actual suspend fun getSoundData(soundId: String, fileName: String): SoundFileInfo {
        return withContext(Dispatchers.IO) {
            val cached = File(cacheDir, "${soundId}.wav")
            if (cached.exists() && cached.length() > 1000) {
                return@withContext SoundFileInfo(
                    soundId = soundId,
                    source = SoundSource.DOWNLOADED,
                    filePath = cached.absolutePath
                )
            }

            val urls = SoundUrlConfig.getDownloadUrls(soundId)
            for (urlStr in urls) {
                try {
                    val downloaded = downloadFile(urlStr, File(cacheDir, "${soundId}.wav"))
                    if (downloaded != null) {
                        return@withContext SoundFileInfo(
                            soundId = soundId,
                            source = SoundSource.DOWNLOADED,
                            filePath = downloaded.absolutePath
                        )
                    }
                } catch (_: Exception) {
                    continue
                }
            }

            SoundFileInfo(soundId = soundId, source = SoundSource.GENERATED)
        }
    }

    actual fun getCachedPath(soundId: String): String? {
        val cached = File(cacheDir, "${soundId}.wav")
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

    private fun downloadFile(urlStr: String, outputFile: File): File? {
        return try {
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 15000
            connection.readTimeout = 30000
            connection.setRequestProperty("User-Agent", "Noctra/1.0")

            if (connection.responseCode == 200) {
                val inputStream = connection.inputStream
                FileOutputStream(outputFile).use { output ->
                    inputStream.use { input ->
                        input.copyTo(output)
                    }
                }
                if (outputFile.length() > 1000) {
                    outputFile
                } else {
                    outputFile.delete()
                    null
                }
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}
