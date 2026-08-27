package com.pascal.noctra.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toKotlinByteArray(): ByteArray {
    val length = this.length.toInt()
    if (length == 0) return ByteArray(0)
    return ByteArray(length).also { arr ->
        arr.usePinned { pinned ->
            memcpy(pinned.addressOf(0), this.bytes, this.length)
        }
    }
}

private fun resolveTopMost(vc: UIViewController): UIViewController {
    return when {
        vc.presentedViewController != null -> resolveTopMost(vc.presentedViewController!!)
        vc is UINavigationController -> resolveTopMost(vc.visibleViewController ?: vc)
        vc is UITabBarController -> resolveTopMost(vc.selectedViewController ?: vc)
        else -> vc
    }
}

private fun topMostViewController(): UIViewController? {
    val root = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return null
    return resolveTopMost(root)
}

@OptIn(ExperimentalForeignApi::class)
private class ImagePickerDelegate(
    private val namePrefix: String,
    private val onResult: (ByteArray?, String) -> Unit,
) : NSObject(),
    UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>,
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        val bytes = image
            ?.let { UIImageJPEGRepresentation(it, 0.85) }
            ?.toKotlinByteArray()
        val name = "${namePrefix}_${NSDate().timeIntervalSince1970.toLong()}.jpg"
        picker.dismissViewControllerAnimated(true, null)
        onResult(bytes, name)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, null)
        onResult(null, "")
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberCameraCapture(
    onResult: (ByteArray?, String) -> Unit,
): CameraCaptureState {
    return remember {
        object : CameraCaptureState {
            private var delegate: ImagePickerDelegate? = null

            override fun launch() {
                if (!UIImagePickerController.isSourceTypeAvailable(
                        UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                    )
                ) {
                    onResult(null, "")
                    return
                }

                val presenter = topMostViewController() ?: return
                val pickerDelegate = ImagePickerDelegate("photo", onResult)
                delegate = pickerDelegate

                val picker = UIImagePickerController().apply {
                    sourceType =
                        UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                    this.delegate = pickerDelegate
                }
                presenter.presentViewController(picker, animated = true, completion = null)
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePicker(
    onResult: (ByteArray?, String) -> Unit,
): ImagePickerState {
    return remember {
        object : ImagePickerState {
            private var delegate: ImagePickerDelegate? = null

            override fun launch() {
                val presenter = topMostViewController() ?: return
                val pickerDelegate = ImagePickerDelegate("gallery", onResult)
                delegate = pickerDelegate

                val picker = UIImagePickerController().apply {
                    sourceType =
                        UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                    this.delegate = pickerDelegate
                }
                presenter.presentViewController(picker, animated = true, completion = null)
            }
        }
    }
}