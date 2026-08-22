package com.pascal.noctra.ui.navigation.route

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pascal.noctra.ui.navigation.screen.BaseScreen
import com.pascal.noctra.ui.screen.home.HomeRoute
import com.pascal.noctra.ui.screen.mixer.MixerRoute
import com.pascal.noctra.ui.screen.onboarding.OnboardingRoute
import com.pascal.noctra.ui.screen.settings.SettingsRoute
import com.pascal.noctra.ui.screen.splash.SplashRoute
import com.pascal.noctra.ui.theme.NocturneAccent
import com.pascal.noctra.ui.theme.NocturneTextMuted
import com.russhwolf.settings.Settings

private data class BottomNavItem(
    val title: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val screen: BaseScreen
)

private val bottomNavItems = listOf(
    BottomNavItem("Discover", Icons.Filled.Home, Icons.Outlined.Home, BaseScreen.HomeScreen),
    BottomNavItem("Mixer", Icons.Filled.MusicNote, Icons.Outlined.MusicNote, BaseScreen.MixerScreen)
)

@Composable
fun NavBaseRoute(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val settings: Settings = remember { com.pascal.noctra.createSettings() }
    val onboardingCompleted = remember { settings.getBoolean("onboarding_completed", false) }
    val startDest = if (onboardingCompleted) BaseScreen.HomeScreen.route else BaseScreen.SplashScreen.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (currentRoute in listOf(BaseScreen.HomeScreen.route, BaseScreen.MixerScreen.route)) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.screen.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.iconFilled else item.iconOutlined,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = selected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NocturneAccent,
                                selectedTextColor = NocturneAccent,
                                indicatorColor = NocturneAccent.copy(alpha = 0.12f),
                                unselectedIconColor = NocturneTextMuted,
                                unselectedTextColor = NocturneTextMuted
                            ),
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
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
            navController = navController,
            startDestination = startDest
        ) {
            composable(BaseScreen.SplashScreen.route) {
                SplashRoute(paddingValues = paddingValues) {
                    navController.navigate(BaseScreen.OnboardingScreen.route) {
                        popUpTo(BaseScreen.SplashScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            composable(BaseScreen.OnboardingScreen.route) {
                OnboardingRoute(onComplete = {
                    settings.putBoolean("onboarding_completed", true)
                    navController.navigate(BaseScreen.HomeScreen.route) {
                        popUpTo(BaseScreen.OnboardingScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }
            composable(BaseScreen.HomeScreen.route) {
                HomeRoute(
                    onNavigateToMixer = { navController.navigate(BaseScreen.MixerScreen.route) },
                    onNavigateToSettings = { navController.navigate(BaseScreen.SettingsScreen.route) }
                )
            }
            composable(BaseScreen.MixerScreen.route) {
                MixerRoute(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToHome = { navController.navigate(BaseScreen.HomeScreen.route) }
                )
            }
            composable(BaseScreen.SettingsScreen.route) {
                SettingsRoute(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
