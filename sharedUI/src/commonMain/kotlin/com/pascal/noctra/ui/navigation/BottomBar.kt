package com.pascal.noctra.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pascal.noctra.ui.navigation.screen.BaseScreen

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val navigationItems = listOf(
                NavigationItem(
                    title = "Discover",
                    iconFilled = Icons.Filled.Home,
                    iconOutlined = Icons.Outlined.Home,
                    screen = BaseScreen.HomeScreen
                ),
                NavigationItem(
                    title = "Mixer",
                    iconFilled = Icons.Filled.MusicNote,
                    iconOutlined = Icons.Outlined.MusicNote,
                    screen = BaseScreen.MixerScreen
                ),
                NavigationItem(
                    title = "Favorite",
                    iconFilled = Icons.Filled.Favorite,
                    iconOutlined = Icons.Outlined.Favorite,
                    screen = BaseScreen.FavoriteScreen
                ),
                NavigationItem(
                    title = "Settings",
                    iconFilled = Icons.Filled.Settings,
                    iconOutlined = Icons.Outlined.Settings,
                    screen = BaseScreen.SettingsScreen
                )
            )

            navigationItems.forEach { item ->
                val selected = currentRoute == item.screen.route
                NavigationBarItem(
                    icon = {
                        val iconSize = if (selected) 26.dp else 24.dp
                        Icon(
                            imageVector = if (selected) item.iconFilled else item.iconOutlined,
                            contentDescription = item.title,
                            modifier = Modifier.size(iconSize),
                            tint = if (selected) MaterialTheme.colorScheme.onSurface else Color.Gray
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 10.sp
                            ),
                            color = if (selected) MaterialTheme.colorScheme.onSurface else Color.Gray
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    ),
                    alwaysShowLabel = false,
                    selected = selected,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
