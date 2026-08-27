package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SelectableTextBorderComponent(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean = false,
    icon: DrawableResource? = null,
    shape: RoundedCornerShape = RoundedCornerShape(50),
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    val primary = MaterialTheme.colorScheme.primary

    val backgroundColor = if (isSelected) primary else Color.Transparent
    val borderColor = if (isSelected) Color.Transparent else primary
    val textColor = if (isSelected) Color.White else primary

    Row(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape)
            .border(1.dp, borderColor, shape)
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        icon?.let {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(16.dp)
            )
        }

        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
