package com.pascal.noctra.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.pascal.noctra.ContextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object AppInfo {
    actual val versionName: String
        get() = ContextUtils.context.packageManager
            .getPackageInfo(ContextUtils.context.packageName, 0)
            .versionName ?: ""

    actual val versionCode: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ContextUtils.context.packageManager
                .getPackageInfo(ContextUtils.context.packageName, 0)
                .longVersionCode.toString()
        } else {
            ContextUtils.context.packageManager
                .getPackageInfo(ContextUtils.context.packageName, 0)
                .versionCode.toString()
        }

    actual val appId: String
        get() = ContextUtils.context.packageName
}

actual fun showToast(msg: String) {
    Toast.makeText(ContextUtils.context, msg, Toast.LENGTH_SHORT).show()
}

actual fun actionShareUrl(url: String?) {
    if (url.isNullOrEmpty()) return

    val context = ContextUtils.context

    Handler(Looper.getMainLooper()).post {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url)
            }

            val chooser = Intent.createChooser(shareIntent, "Bagikan link ke...")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

actual fun openAppSettings() {
    val context = ContextUtils.context
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Intent(android.provider.Settings.ACTION_APP_LOCALE_SETTINGS).apply {
            data = android.net.Uri.parse("package:${context.packageName}")
        }
    } else {
        Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.parse("package:${context.packageName}")
        }
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

actual fun downloadDirectory(): String =
    android.os.Environment
        .getExternalStoragePublicDirectory(
            android.os.Environment.DIRECTORY_DOWNLOADS
        ).absolutePath

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
actual fun isOnline(): Boolean {
    val connectivityManager =
        ContextUtils.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

actual suspend fun decodeByteArrayToImageBitmap(bytes: ByteArray): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
    }
}
