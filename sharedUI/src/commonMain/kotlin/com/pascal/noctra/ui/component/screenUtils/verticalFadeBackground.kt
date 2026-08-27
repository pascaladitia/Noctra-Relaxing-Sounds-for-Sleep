package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.verticalFadeBackground(
    startColor: Color = Color.Black,
    isTop: Boolean = true
): Modifier = this.background(
    brush = Brush.verticalGradient(
        colors = if (isTop) {
            listOf(
                startColor,
                startColor.copy(alpha = startColor.alpha * 0.9f),
                startColor.copy(alpha = startColor.alpha * 0.6f),
                Color.Transparent
            )
        } else {
            listOf(
                Color.Transparent,
                startColor.copy(alpha = startColor.alpha * 0.6f),
                startColor.copy(alpha = startColor.alpha * 0.9f),
                startColor
            )
        }
    )
)

