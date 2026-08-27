package com.pascal.noctra.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication
import platform.UIKit.UIRectEdgeLeft
import platform.UIKit.UIScreenEdgePanGestureRecognizer
import platform.UIKit.UIGestureRecognizerStateEnded

@Composable
@OptIn(ExperimentalForeignApi::class)
actual fun AppBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    val latestOnBack by rememberUpdatedState(onBack)
    val rootView = UIApplication.sharedApplication.keyWindow?.rootViewController?.view

    DisposableEffect(rootView, enabled) {
        if (!enabled || rootView == null) return@DisposableEffect onDispose { }

        val target = BackGestureTarget {
            latestOnBack()
        }

        val edgeBackGesture = UIScreenEdgePanGestureRecognizer(
            target = target,
            action = NSSelectorFromString("handleEdgeBackGesture:")
        ).apply {
            edges = UIRectEdgeLeft
        }

        rootView.addGestureRecognizer(edgeBackGesture)

        onDispose {
            rootView.removeGestureRecognizer(edgeBackGesture)
        }
    }
}

private class BackGestureTarget(
    private val onBack: () -> Unit
) : platform.darwin.NSObject() {
    @Suppress("UNUSED_PARAMETER")
    @ObjCAction
    fun handleEdgeBackGesture(gesture: UIScreenEdgePanGestureRecognizer) {
        if (gesture.state == UIGestureRecognizerStateEnded) {
            onBack()
        }
    }
}
