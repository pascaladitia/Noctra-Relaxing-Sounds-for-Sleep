package com.pascal.noctra.utils.download

interface Downloader {
    fun download(url: String)
    fun download(bytes: ByteArray)
    fun downloadPdf(bytes: ByteArray, fileName: String = "report.pdf")
}

expect class DownloadManager() : Downloader {
    override fun download(url: String)
    override fun download(bytes: ByteArray)
    override fun downloadPdf(bytes: ByteArray, fileName: String)
}
