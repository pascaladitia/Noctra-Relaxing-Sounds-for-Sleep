package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.platform.annotation.SuppressLint

@Composable
fun Modifier.shimmer(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer-transition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "shimmer-translate"
    )

    val shimmerColors = listOf(
        Color.LightGray,
        Color.Gray,
        Color.LightGray,
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(0f, 0f),
        end = Offset(translateAnim, translateAnim)
    )

    return this.background(brush = brush)
}

fun Modifier.topShadow(
    height: Dp = 12.dp,
    color: Color = Color.Black.copy(alpha = 0.18f),
): Modifier {
    return this
        .drawWithContent {
            drawContent()

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color,
                        Color.Transparent,
                    ),
                    startY = 0f,
                    endY = height.toPx(),
                ),
                topLeft = Offset(
                    x = 0f,
                    y = 0f,
                ),
                size = Size(
                    width = size.width,
                    height = height.toPx(),
                ),
            )
        }
}

fun Modifier.noRippleClickable(onClick: () -> Unit) = this.clickable(
    interactionSource = MutableInteractionSource(),
    indication = null,
    onClick = onClick
)
