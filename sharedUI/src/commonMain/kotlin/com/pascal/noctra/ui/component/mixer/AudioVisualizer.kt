package com.pascal.noctra.ui.component.mixer

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pascal.noctra.ui.theme.NocturneAccent

@Composable
fun AudioVisualizer(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 30,
    barColor: Color = NocturneAccent
) {
    val infiniteTransition = rememberInfiniteTransition()

    val animValues = (0 until barCount).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = if (isActive) {
                when (index % 4) {
                    0 -> 0.9f
                    1 -> 0.5f
                    2 -> 0.7f
                    else -> 0.3f
                }
            } else 0.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 600 + (index * 50) % 400,
                    delayMillis = (index * 80) % 300,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Canvas(modifier = modifier.fillMaxWidth().height(64.dp)) {
        val barWidth = (size.width / barCount) * 0.6f
        val spacing = (size.width / barCount) * 0.4f
        val centerY = size.height / 2

        animValues.forEachIndexed { index, animValue ->
            val barHeight = animValue.value * size.height * 0.8f
            val x = index * (barWidth + spacing)

            drawRoundRect(
                color = barColor.copy(alpha = 0.3f + animValue.value * 0.7f),
                topLeft = Offset(x, centerY - barHeight / 2),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}
