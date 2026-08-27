package com.pascal.noctra.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.pascal.noctra.ui.navigation.screen.BaseScreen

data class NavigationItem(
    val title: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val screen: BaseScreen
)
