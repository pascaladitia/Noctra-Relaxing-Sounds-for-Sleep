package com.pascal.noctra.utils.pdf

import com.pascal.noctra.utils.toNSData
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIImage
import platform.UIKit.UIGraphicsBeginPDFContextToFile
import platform.UIKit.UIGraphicsBeginPDFPageWithInfo
import platform.UIKit.UIGraphicsEndPDFContext

@OptIn(ExperimentalForeignApi::class)
actual suspend fun printMangaPdf(
    url: List<String>,
    fileName: String
): Result<String> = withContext(Dispatchers.IO) {
    runCatching {
        val images = url.filter { it.isNotBlank() }
        require(images.isNotEmpty()) { "Image manga tidak tersedia" }

        val path = "${documentsDirectory()}/${fileName.ensurePdfExtension()}"

        UIGraphicsBeginPDFContextToFile(path, CGRectMake(0.0, 0.0, 1.0, 1.0), null)

        val client = HttpClient(Darwin)
        try {
            try {
                if (images.size >= 2) {
                    val firstImage = images[0].loadImage(client)
                    val secondImage = images[1].loadImage(client)
                    addCombinedPage(firstImage, secondImage)
                } else {
                    addFullImagePage(images.first().loadImage(client))
                }

                images.drop(2).forEach { imageUrl ->
                    addFullImagePage(imageUrl.loadImage(client))
                }
            } finally {
                client.close()
            }
        } finally {
            UIGraphicsEndPDFContext()
        }

        path
    }
}

private fun documentsDirectory(): String {
    return NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory,
        NSUserDomainMask,
        true
    ).first() as String
}

private suspend fun String.loadImage(client: HttpClient): UIImage {
    val data = client.get(this).bodyAsBytes().toNSData()
    return UIImage.imageWithData(data) ?: error("Gagal membaca gambar manga")
}

@OptIn(ExperimentalForeignApi::class)
private fun addCombinedPage(first: UIImage, second: UIImage) {
    val firstSize = first.size.useContents { width to height }
    val secondSize = second.size.useContents { width to height }
    val pageWidth = maxOf(firstSize.first, secondSize.first)
    val firstHeight = firstSize.second * (pageWidth / firstSize.first)
    val secondHeight = secondSize.second * (pageWidth / secondSize.first)
    val pageHeight = firstHeight + secondHeight

    UIGraphicsBeginPDFPageWithInfo(CGRectMake(0.0, 0.0, pageWidth, pageHeight), null)
    first.drawInRect(CGRectMake(0.0, 0.0, pageWidth, firstHeight))
    second.drawInRect(CGRectMake(0.0, firstHeight, pageWidth, secondHeight))
}

@OptIn(ExperimentalForeignApi::class)
private fun addFullImagePage(image: UIImage) {
    val imageSize = image.size.useContents { width to height }
    UIGraphicsBeginPDFPageWithInfo(CGRectMake(0.0, 0.0, imageSize.first, imageSize.second), null)
    image.drawInRect(CGRectMake(0.0, 0.0, imageSize.first, imageSize.second))
}

private fun String.ensurePdfExtension(): String {
    return if (endsWith(".pdf", ignoreCase = true)) this else "$this.pdf"
}
