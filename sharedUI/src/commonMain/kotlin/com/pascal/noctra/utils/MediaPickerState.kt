package com.pascal.noctra.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getDrawableResourceBytes

@Stable
interface CameraCaptureState {
    fun launch()
}

@Composable
expect fun rememberCameraCapture(
    onResult: (ByteArray?, String) -> Unit,
): CameraCaptureState

@Stable
interface ImagePickerState {
    fun launch()
}

@Composable
expect fun rememberImagePicker(
    onResult: (ByteArray?, String) -> Unit,
): ImagePickerState

suspend fun DrawableResource.toByteArray(
    environment: ResourceEnvironment
): ByteArray = getDrawableResourceBytes(environment, this)