package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

@Composable
fun AnchoredPopup(
    modifier: Modifier = Modifier,
    anchor: @Composable (onClick: () -> Unit) -> Unit,
    popupContent: @Composable (() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var anchorCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(modifier) {

        Box(
            modifier = Modifier.onGloballyPositioned {
                anchorCoordinates = it
            }
        ) {
            anchor {
                expanded = !expanded
            }
        }

        if (expanded && anchorCoordinates != null) {
            Popup(
                popupPositionProvider = remember(anchorCoordinates) {
                    object : PopupPositionProvider {
                        override fun calculatePosition(
                            anchorBounds: IntRect,
                            windowSize: IntSize,
                            layoutDirection: LayoutDirection,
                            popupContentSize: IntSize
                        ): IntOffset {
                            return IntOffset(
                                x = anchorBounds.left,
                                y = anchorBounds.bottom
                            )
                        }
                    }
                },
                onDismissRequest = { expanded = false }
            ) {
                Box(
                    modifier = Modifier
                        .shadow(16.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                ) {
                    popupContent {
                        expanded = false
                    }
                }
            }
        }
    }
}


