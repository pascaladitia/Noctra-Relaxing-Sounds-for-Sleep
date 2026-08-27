package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pascal.noctra.ui.theme.AppTheme

@Composable
fun RatingScore(
    score: Double,
    modifier: Modifier = Modifier,
    maxScore: Double = 10.0,
    starCount: Int = 5,
    starSize: Dp = 20.dp,
    space: Dp = 4.dp
) {
    val safeScore = score.coerceIn(0.0, maxScore)
    val rating = (safeScore / maxScore) * starCount

    val fullStars = rating.toInt()
    val hasHalfStar = rating - fullStars in 0.5..0.99
    val emptyStars = starCount - fullStars - if (hasHalfStar) 1 else 0

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(starSize)
            )
            Spacer(modifier = Modifier.width(space))
        }

        if (hasHalfStar) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(starSize)
            )
            Spacer(modifier = Modifier.width(space))
        }

        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(starSize)
            )
            Spacer(modifier = Modifier.width(space))
        }
    }
}

@Preview
@Composable
private fun RatingScorePreview() {
    AppTheme {
        RatingScore(
            score = 7.0
        )
    }
}
