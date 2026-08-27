package com.pascal.noctra.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
actual fun rememberCameraCapture(
    onResult: (ByteArray?, String) -> Unit,
): CameraCaptureState {
    val context = LocalContext.current
    val currentOnResult = rememberUpdatedState(onResult)

    val tempUriState = remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val uri   = tempUriState.value
            val bytes = uri?.let {
                try {
                    val name = "photo_${System.currentTimeMillis()}.jpg"
                    val tempFile = File(context.cacheDir, name)
                    context.contentResolver.openInputStream(it)?.use { input ->
                        tempFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    val data = if (tempFile.exists()) tempFile.readBytes() else null
                    tempFile.delete()
                    data
                } catch (_: Exception) {
                    null
                }
            }
            val name  = "photo_${System.currentTimeMillis()}.jpg"
            currentOnResult.value(bytes, name)
        } else {
            currentOnResult.value(null, "")
        }
        tempUriState.value = null
    }

    return remember(launcher) {
        object : CameraCaptureState {
            override fun launch() {
                val file = createTempImageFile(context)
                val uri  = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file,
                )
                tempUriState.value = uri
                launcher.launch(uri)
            }
        }
    }
}

@Composable
actual fun rememberImagePicker(
    onResult: (ByteArray?, String) -> Unit,
): ImagePickerState {
    val context = LocalContext.current
    val currentOnResult = rememberUpdatedState(onResult)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val name = "gallery_${System.currentTimeMillis()}.jpg"
                val tempFile = File(context.cacheDir, name)
                context.contentResolver.openInputStream(uri)?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                val bytes = if (tempFile.exists()) tempFile.readBytes() else null
                tempFile.delete()
                currentOnResult.value(bytes, name)
            } catch (_: Exception) {
                currentOnResult.value(null, "")
            }
        } else {
            currentOnResult.value(null, "")
        }
    }

    return remember(launcher) {
        object : ImagePickerState {
            override fun launch() = launcher.launch(arrayOf("image/*"))
        }
    }
}

private fun createTempImageFile(context: Context): File {
    val ts  = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(Date())
    val dir = context.getExternalFilesDir("Pictures") ?: context.filesDir
    dir.mkdirs()
    return File.createTempFile("IMG_${ts}_", ".jpg", dir)
}
