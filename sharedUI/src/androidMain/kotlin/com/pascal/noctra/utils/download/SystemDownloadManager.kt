package com.pascal.noctra.utils.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import androidx.core.net.toUri
import com.pascal.noctra.ContextUtils
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SystemDownloadManager {

    private val context: Context = ContextUtils.context
    private val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    @SuppressLint("NewApi")
    fun download(url: String) {
        val fileName = url.substringAfterLast("/").ifBlank { "file.bin" }

        val request = DownloadManager.Request(url.toUri())
            .setTitle(fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )

        val downloadId = dm.enqueue(request)

        context.registerReceiver(
            DownloadCompleteReceiver(downloadId),
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            if (Build.VERSION.SDK_INT >= 33)
                Context.RECEIVER_NOT_EXPORTED else 0
        )
    }

    fun download(bytes: ByteArray) {
        val fileName = generateFileName()
        writeBytesToDownloads(bytes, fileName)
    }

    fun downloadPdf(bytes: ByteArray, fileName: String) {
        writeBytesToDownloads(bytes, fileName.asPdfFileName())
    }

    private fun writeBytesToDownloads(bytes: ByteArray, fileName: String) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )

        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }

        val file = File(downloadsDir, fileName)

        FileOutputStream(file).use {
            it.write(bytes)
            it.flush()
        }

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = file.toUri()
        context.sendBroadcast(intent)
    }

    private fun generateFileName(): String {
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return "file_$time.bin"
    }

    private fun String.asPdfFileName(): String {
        val trimmed = trim().ifBlank {
            val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            "report_$time.pdf"
        }
        return if (trimmed.endsWith(".pdf", ignoreCase = true)) trimmed else "$trimmed.pdf"
    }

    private inner class DownloadCompleteReceiver(
        private val downloadId: Long
    ) : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id != downloadId) return
            context.unregisterReceiver(this)
        }
    }
}
