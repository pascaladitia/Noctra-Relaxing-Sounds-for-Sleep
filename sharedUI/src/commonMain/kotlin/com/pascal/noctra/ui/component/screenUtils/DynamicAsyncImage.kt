package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.pascal.noctra.utils.download.DownloadManager
import com.pascal.noctra.utils.showToast

@Composable
fun DynamicAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    imageBytes: ByteArray? = null,
    contentDescription: String? = null,
    placeholder: Painter? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalPlatformContext.current
    val imageLoader = remember(context) { getAsyncImageLoader(context) }
    val model = imageBytes ?: imageUrl.takeIf { it.isNotBlank() }

    var isLoading by remember(model) { mutableStateOf(model != null) }

    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model,
            contentDescription = contentDescription,
            imageLoader = imageLoader,
            placeholder = placeholder,
            error = placeholder,
            contentScale = contentScale,
            onLoading = {
                isLoading = true
            },
            onSuccess = {
                isLoading = false
            },
            onError = {
                isLoading = false
            }
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer()
            )
        }
    }
}

@Composable
fun DynamicZoomableAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    imageBytes: ByteArray? = null,
    contentDescription: String? = null,
    placeholder: Painter? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val context = LocalPlatformContext.current
    val downloader = remember { DownloadManager() }
    var showViewer by remember { mutableStateOf(false) }

    val imageLoader = remember(context) { getAsyncImageLoader(context) }
    val model = imageBytes ?: imageUrl.takeIf { it.isNotBlank() }

    var isLoading by remember(model) { mutableStateOf(model != null) }

    var scale by remember(model) { mutableFloatStateOf(1f) }
    var offset by remember(model) { mutableStateOf(Offset.Zero) }

    if (showViewer) {
        Dialog(
            onDismissRequest = { showViewer = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = modifier.pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 4f)
                            offset = if (scale == 1f) Offset.Zero else offset + pan * scale
                        }
                    }
                ) {
                    AsyncImage(
                        model = model,
                        contentDescription = contentDescription,
                        imageLoader = imageLoader,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = offset.x
                                translationY = offset.y
                            },
                        placeholder = placeholder,
                        error = placeholder,
                        contentScale = ContentScale.Fit,
                        onLoading = {
                            isLoading = true
                        },
                        onSuccess = {
                            isLoading = false
                        },
                        onError = {
                            isLoading = false
                        }
                    )

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmer()
                        )
                    }
                }

                IconButton(
                    onClick = {
                        when {
                            imageBytes != null -> {
                                downloader.download(imageBytes)
                            }

                            imageUrl.isNotBlank() -> {
                                downloader.download(imageUrl)
                            }
                        }

                        showToast("Download Success...")
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(0.5f), RoundedCornerShape(16.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }

                IconButton(
                    onClick = { showViewer = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(0.5f), RoundedCornerShape(16.dp))
                        .padding(8.dp),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier.clickable {
            showViewer = true
        }
    ) {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            imageLoader = imageLoader,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                },
            placeholder = placeholder,
            error = placeholder,
            contentScale = contentScale,
            onLoading = {
                isLoading = true
            },
            onSuccess = {
                isLoading = false
            },
            onError = {
                isLoading = false
            }
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer()
            )
        }
    }
}

fun getAsyncImageLoader(context: PlatformContext): ImageLoader {
    return ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, 0.20)
                .strongReferencesEnabled(false)
                .build()
        }
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .crossfade(true)
        .logger(DebugLogger())
        .build()
}
