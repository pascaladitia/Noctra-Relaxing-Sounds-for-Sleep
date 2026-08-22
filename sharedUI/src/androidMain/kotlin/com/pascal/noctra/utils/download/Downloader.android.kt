package com.pascal.noctra.utils.download

actual class DownloadManager actual constructor() : Downloader {

    private val systemDownloader = SystemDownloadManager()

    actual override fun download(url: String) {
        systemDownloader.download(url)
    }

    actual override fun download(bytes: ByteArray) {
        systemDownloader.download(bytes)
    }

    actual override fun downloadPdf(bytes: ByteArray, fileName: String) {
        systemDownloader.downloadPdf(bytes, fileName)
    }
}
