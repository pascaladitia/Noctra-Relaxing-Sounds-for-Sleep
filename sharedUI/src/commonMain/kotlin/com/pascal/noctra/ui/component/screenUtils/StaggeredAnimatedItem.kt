package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

enum class SlideDirection {
    NONE,
    LEFT,
    RIGHT,
    UP,
    DOWN,
}

enum class StaggeredAnimEffect {
    NONE,
    FADE,
    SCALE,
    ROTATE,
    FADE_SCALE,
    FADE_ROTATE,
    SCALE_ROTATE,
    FADE_SCALE_ROTATE,
}

private val LocalStaggeredVisible = staticCompositionLocalOf { true }
private val LocalStaggeredTotalItems = staticCompositionLocalOf { 1 }
private val LocalStaggeredEffect = staticCompositionLocalOf { StaggeredAnimEffect.FADE }
private val LocalStaggeredSlideDirection = staticCompositionLocalOf { SlideDirection.RIGHT }

@Composable
fun StaggeredScope(
    visible: Boolean,
    totalItems: Int,
    slideDirection: SlideDirection = SlideDirection.RIGHT,
    effect: StaggeredAnimEffect = StaggeredAnimEffect.FADE,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalStaggeredVisible provides visible,
        LocalStaggeredTotalItems provides totalItems,
        LocalStaggeredEffect provides effect,
        LocalStaggeredSlideDirection provides slideDirection,
        content = content,
    )
}

@Composable
fun StaggeredAnimatedItem(
    modifier: Modifier = Modifier,
    index: Int,
    visible: Boolean = LocalStaggeredVisible.current,
    totalItems: Int = LocalStaggeredTotalItems.current,
    effect: StaggeredAnimEffect = LocalStaggeredEffect.current,
    slideDirection: SlideDirection = LocalStaggeredSlideDirection.current,
    staggerMillis: Int = 48,
    enterDurationMillis: Int = 300,
    exitDurationMillis: Int = 240,
    hiddenOffsetPx: Float = 72f,
    rotationDegrees: Float = 360f,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable () -> Unit,
) {
    val delayMillis = if (visible) {
        index * staggerMillis
    } else {
        (totalItems - 1 - index) * staggerMillis
    }
    val durationMillis = if (visible) enterDurationMillis else exitDurationMillis

    val targetAlpha = when (effect) {
        StaggeredAnimEffect.FADE,
        StaggeredAnimEffect.FADE_SCALE,
        StaggeredAnimEffect.FADE_ROTATE,
        StaggeredAnimEffect.FADE_SCALE_ROTATE -> if (visible) 1f else 0f
        else -> 1f
    }
    val targetTranslationX = when (slideDirection) {
        SlideDirection.RIGHT -> if (visible) 0f else hiddenOffsetPx
        SlideDirection.LEFT -> if (visible) 0f else -hiddenOffsetPx
        else -> 0f
    }
    val targetTranslationY = when (slideDirection) {
        SlideDirection.UP -> if (visible) 0f else -hiddenOffsetPx
        SlideDirection.DOWN -> if (visible) 0f else hiddenOffsetPx
        else -> 0f
    }
    val targetScale = when (effect) {
        StaggeredAnimEffect.SCALE,
        StaggeredAnimEffect.FADE_SCALE,
        StaggeredAnimEffect.SCALE_ROTATE,
        StaggeredAnimEffect.FADE_SCALE_ROTATE -> if (visible) 1f else 0f
        else -> 1f
    }
    val targetRotation = when (effect) {
        StaggeredAnimEffect.ROTATE,
        StaggeredAnimEffect.FADE_ROTATE,
        StaggeredAnimEffect.SCALE_ROTATE,
        StaggeredAnimEffect.FADE_SCALE_ROTATE -> if (visible) 0f else rotationDegrees
        else -> 0f
    }

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "staggeredItemAlpha",
    )
    val translationX by animateFloatAsState(
        targetValue = targetTranslationX,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "staggeredItemTranslationX",
    )
    val translationY by animateFloatAsState(
        targetValue = targetTranslationY,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "staggeredItemTranslationY",
    )
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "staggeredItemScale",
    )
    val rotation by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "staggeredItemRotation",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha
                this.translationX = translationX
                this.translationY = translationY
                this.scaleX = scale
                this.scaleY = scale
                this.rotationZ = rotation
            },
        horizontalAlignment = horizontalAlignment,
    ) {
        content()
    }
}
