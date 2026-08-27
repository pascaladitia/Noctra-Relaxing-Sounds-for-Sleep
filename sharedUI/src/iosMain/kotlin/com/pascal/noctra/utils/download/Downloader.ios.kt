package com.pascal.noctra.utils.download

import co.touchlab.kermit.Logger
import com.pascal.noctra.utils.toNSData
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLSession
import platform.Foundation.NSURLSessionConfiguration
import platform.Foundation.NSURLSessionDownloadDelegateProtocol
import platform.Foundation.NSURLSessionDownloadTask
import platform.Foundation.NSURLSessionTask
import platform.Foundation.NSUserDomainMask
import platform.darwin.NSObject
import platform.Foundation.create
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToURL

actual class DownloadManager actual constructor() : Downloader {

    private val logger = Logger.withTag("DownloadManager-iOS")
    private val delegate = DownloadDelegate(logger)

    private val session: NSURLSession by lazy {
        logger.i("Creating native NSURLSession (delegate based)")
        NSURLSession.sessionWithConfiguration(
            configuration = NSURLSessionConfiguration.defaultSessionConfiguration(),
            delegate = delegate,
            delegateQueue = NSOperationQueue.mainQueue()
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    actual override fun download(url: String) {
        logger.i("Download called: $url")

        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl == null) {
            logger.e("Invalid URL")
            return
        }

        val request = NSURLRequest.requestWithURL(nsUrl)
        val task = session.downloadTaskWithRequest(request)

        logger.i("Resuming download task")
        task.resume()
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual override fun download(bytes: ByteArray) {
        writeBytes(bytes, generateFileName())
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual override fun downloadPdf(bytes: ByteArray, fileName: String) {
        writeBytes(bytes, fileName.asPdfFileName())
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun writeBytes(bytes: ByteArray, fileName: String) {
        val fileManager = NSFileManager.defaultManager

        val documentsDir = fileManager
            .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .firstOrNull() as? NSURL
            ?: run {
                logger.e("Documents directory not found")
                return
            }

        val destinationUrl =
            documentsDir.URLByAppendingPathComponent(fileName)
                ?: run {
                    logger.e("Failed to create destination URL")
                    return
                }

        destinationUrl.path?.let {
            if (fileManager.fileExistsAtPath(it)) {
                fileManager.removeItemAtURL(destinationUrl, null)
            }
        }

        val data = NSData.create(bytes.toNSData())
        val success = data.writeToURL(destinationUrl, atomically = true)

        if (success) {
            logger.i("File saved at: ${destinationUrl.path}")
        } else {
            logger.e("Write file failed")
        }
    }

    private fun generateFileName(): String {
        val time = (NSDate().timeIntervalSince1970 * 1000).toLong()
        return "file_$time.bin"
    }

    private fun String.asPdfFileName(): String {
        val trimmed = trim().ifBlank {
            val time = (NSDate().timeIntervalSince1970 * 1000).toLong()
            "report_$time.pdf"
        }
        return if (trimmed.endsWith(".pdf", ignoreCase = true)) trimmed else "$trimmed.pdf"
    }
}

private class DownloadDelegate(
    private val logger: Logger
) : NSObject(), NSURLSessionDownloadDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    override fun URLSession(
        session: NSURLSession,
        downloadTask: NSURLSessionDownloadTask,
        didFinishDownloadingToURL: NSURL
    ) {
        logger.i("Download finished (delegate)")

        val fileManager = NSFileManager.defaultManager

        val documentsDir = fileManager
            .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .firstOrNull() as? NSURL
            ?: run {
                logger.e("Documents directory not found")
                return
            }

        val fileName =
            downloadTask.response?.suggestedFilename
                ?: downloadTask.originalRequest?.URL?.lastPathComponent
                ?: "file.bin"

        val destinationUrl =
            documentsDir.URLByAppendingPathComponent(fileName)
                ?: run {
                    logger.e("Failed to create destination URL")
                    return
                }

        destinationUrl.path?.let {
            if (fileManager.fileExistsAtPath(it)) {
                fileManager.removeItemAtURL(destinationUrl, null)
            }
        }

        val success = fileManager.moveItemAtURL(
            srcURL = didFinishDownloadingToURL,
            toURL = destinationUrl,
            error = null
        )

        if (success) {
            logger.i("File saved at: ${destinationUrl.path}")
        } else {
            logger.e("Move file failed")
        }
    }

    override fun URLSession(
        session: NSURLSession,
        task: NSURLSessionTask,
        didCompleteWithError: NSError?
    ) {
        if (didCompleteWithError != null) {
            logger.e("Download error: ${didCompleteWithError.localizedDescription}")
        } else {
            logger.i("Task completed successfully")
        }
    }
}
