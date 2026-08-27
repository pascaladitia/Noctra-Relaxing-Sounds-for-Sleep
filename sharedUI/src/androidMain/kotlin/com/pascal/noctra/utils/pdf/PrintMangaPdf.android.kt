package com.pascal.noctra.utils.pdf

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.graphics.pdf.PdfDocument
import com.pascal.noctra.ContextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL

actual suspend fun printMangaPdf(
    url: List<String>,
    fileName: String
): Result<String> = withContext(Dispatchers.IO) {
    runCatching {
        val images = url.filter { it.isNotBlank() }
        require(images.isNotEmpty()) { "Image manga tidak tersedia" }

        val pdfBytes = ByteArrayOutputStream()

        val document = PdfDocument()
        try {
            var pageNumber = 1

            if (images.size >= 2) {
                val firstBitmap = images[0].loadBitmap()
                val secondBitmap = images[1].loadBitmap()
                try {
                    document.addCombinedPage(
                        first = firstBitmap,
                        second = secondBitmap,
                        pageNumber = pageNumber
                    )
                    pageNumber++
                } finally {
                    firstBitmap.recycle()
                    secondBitmap.recycle()
                }
            } else {
                val bitmap = images.first().loadBitmap()
                try {
                    document.addFullImagePage(bitmap, pageNumber)
                } finally {
                    bitmap.recycle()
                }
                pageNumber++
            }

            images.drop(2).forEach { imageUrl ->
                val bitmap = imageUrl.loadBitmap()
                try {
                    document.addFullImagePage(bitmap, pageNumber)
                    pageNumber++
                } finally {
                    bitmap.recycle()
                }
            }

            document.writeTo(pdfBytes)
        } finally {
            document.close()
        }

        savePdf(fileName.ensurePdfExtension(), pdfBytes.toByteArray())
    }
}

private fun String.loadBitmap(): Bitmap {
    return BufferedInputStream(URL(this).openStream()).use { input ->
        BitmapFactory.decodeStream(input) ?: error("Gagal membaca gambar manga")
    }
}

private fun PdfDocument.addCombinedPage(
    first: Bitmap,
    second: Bitmap,
    pageNumber: Int
) {
    val pageWidth = maxOf(first.width, second.width)
    val firstHeight = first.scaledHeight(pageWidth)
    val secondHeight = second.scaledHeight(pageWidth)
    val pageHeight = firstHeight + secondHeight
    val page = startPage(
        PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
    )

    page.canvas.drawBitmap(first, null, android.graphics.Rect(0, 0, pageWidth, firstHeight), null)
    page.canvas.drawBitmap(second, null, android.graphics.Rect(0, firstHeight, pageWidth, pageHeight), null)
    finishPage(page)
}

private fun PdfDocument.addFullImagePage(bitmap: Bitmap, pageNumber: Int) {
    val page = startPage(
        PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, pageNumber).create()
    )

    page.canvas.drawBitmap(bitmap, 0f, 0f, null)
    finishPage(page)
}

private fun Bitmap.scaledHeight(targetWidth: Int): Int {
    return (height * (targetWidth.toFloat() / width)).toInt()
}

private fun savePdf(fileName: String, bytes: ByteArray): String {
    val context = ContextUtils.context

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            ?: error("Gagal membuat file PDF")

        resolver.openOutputStream(uri)?.use { it.write(bytes) }
            ?: error("Gagal menulis file PDF")

        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)

        return "${Environment.DIRECTORY_DOWNLOADS}/$fileName"
    }

    val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        ?: context.filesDir
    dir.mkdirs()

    val file = File(dir, fileName)
    file.writeBytes(bytes)
    return file.absolutePath
}

private fun String.ensurePdfExtension(): String {
    return if (endsWith(".pdf", ignoreCase = true)) this else "$this.pdf"
}
