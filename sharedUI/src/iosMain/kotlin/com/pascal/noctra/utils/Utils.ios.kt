package com.pascal.noctra.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithBytes
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UINavigationController
import platform.UIKit.UITabBarController

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object AppInfo {
    actual val versionName: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: ""

    actual val versionCode: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleVersion") as? String ?: ""

    actual val appId: String
        get() = NSBundle.mainBundle.bundleIdentifier ?: ""
}

fun topMostViewController(): UIViewController? {
    val root = UIApplication.sharedApplication
        .windows
        .firstOrNull { (it as? platform.UIKit.UIWindow)?.isKeyWindow() == true }
        ?.let { (it as? platform.UIKit.UIWindow)?.rootViewController }
        ?: return null
    return resolveTopMost(root)
}

private fun resolveTopMost(vc: UIViewController): UIViewController {
    return when {
        vc.presentedViewController != null -> resolveTopMost(vc.presentedViewController!!)
        vc is UINavigationController -> resolveTopMost(vc.visibleViewController ?: vc)
        vc is UITabBarController -> resolveTopMost(vc.selectedViewController ?: vc)
        else -> vc
    }
}

actual fun showToast(msg: String) {
    val presenter = topMostViewController() ?: return

    val alert = UIAlertController.alertControllerWithTitle(
        title = null,
        message = msg,
        preferredStyle = UIAlertControllerStyleAlert
    )
    alert.addAction(
        UIAlertAction.actionWithTitle(
            title = "OK",
            style = UIAlertActionStyleDefault,
            handler = null
        )
    )

    presenter.presentViewController(alert, animated = true, completion = null)
}

actual fun actionShareUrl(url: String?) {
    if (url.isNullOrEmpty()) return

    val items: List<Any> = listOf(url)

    val activityVC = UIActivityViewController(
        activityItems = items,
        applicationActivities = null
    )

    val rootVC = topMostViewController() ?: return

    NSOperationQueue.mainQueue.addOperationWithBlock {
        rootVC.presentViewController(activityVC, animated = true, completion = null)
    }
}

actual fun openAppSettings() {
    UIApplication.sharedApplication.openURL(
        NSURL(string = UIApplicationOpenSettingsURLString)
    )
}

actual fun downloadDirectory(): String =
    NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory,
        NSUserDomainMask,
        true
    ).first() as String

@OptIn(ExperimentalForeignApi::class)
actual fun isOnline(): Boolean {
    val host = "google.com"

    val reachability = SCNetworkReachabilityCreateWithName(null, host)
        ?: return false

    return memScoped {
        val flags = alloc<SCNetworkReachabilityFlagsVar>()

        if (!SCNetworkReachabilityGetFlags(reachability, flags.ptr)) {
            return@memScoped false
        }

        val flagValue = flags.value

        val isReachable =
            (flagValue and kSCNetworkReachabilityFlagsReachable) != 0u

        val needsConnection =
            (flagValue and kSCNetworkReachabilityFlagsConnectionRequired) != 0u

        isReachable && !needsConnection
    }
}

actual suspend fun decodeByteArrayToImageBitmap(bytes: ByteArray): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        runCatching {
            Image.makeFromEncoded(bytes).toComposeImageBitmap()
        }.getOrNull()
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData {
    return this.usePinned { pinned ->
        NSData.dataWithBytes(
            bytes = pinned.addressOf(0),
            length = this.size.toULong()
        )
    }
}