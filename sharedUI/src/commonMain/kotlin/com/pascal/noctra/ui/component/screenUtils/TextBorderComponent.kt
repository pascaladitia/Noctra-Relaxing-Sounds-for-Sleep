package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pascal.noctra.ui.theme.AppTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TextBorderComponent(
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    text: String,
    textColor: Color = Color.White,
    color: Color = MaterialTheme.colorScheme.error,
    paddingValues: PaddingValues = PaddingValues(6.dp),
    shape: RoundedCornerShape = RoundedCornerShape(6.dp)
) {
    Row(
        modifier = modifier
            .background(color, shape)
            .clip(shape)
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Image(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(16.dp),
                painter = painterResource(icon),
                contentDescription = null,
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(
                color = textColor
            ),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun TextBorderPreview() {
    AppTheme {
        TextBorderComponent(text = "TEST")
    }
}
