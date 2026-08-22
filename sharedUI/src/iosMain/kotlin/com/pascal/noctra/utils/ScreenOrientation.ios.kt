package com.pascal.noctra.utils

import platform.Foundation.NSNumber
import platform.Foundation.numberWithInt
import platform.Foundation.setValue
import platform.UIKit.UIDevice
import platform.UIKit.UIInterfaceOrientationLandscapeRight
import platform.UIKit.UIInterfaceOrientationPortrait
import platform.UIKit.UIViewController
import platform.UIKit.attemptRotationToDeviceOrientation

actual fun setScreenOrientation(orientation: ScreenOrientation) {
    val value = when (orientation) {
        ScreenOrientation.PORTRAIT -> UIInterfaceOrientationPortrait
        ScreenOrientation.LANDSCAPE -> UIInterfaceOrientationLandscapeRight
    }

    UIDevice.currentDevice.setValue(
        NSNumber.numberWithInt(value.toInt()),
        forKey = "orientation"
    )

    UIViewController.attemptRotationToDeviceOrientation()
}