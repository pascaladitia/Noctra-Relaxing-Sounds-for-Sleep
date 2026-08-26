package com.pascal.noctra.ui.navigation.route

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pascal.noctra.ui.navigation.BottomBar
import com.pascal.noctra.ui.navigation.screen.BaseScreen
import com.pascal.noctra.ui.screen.home.HomeRoute
import com.pascal.noctra.ui.screen.mixer.MixerRoute
import com.pascal.noctra.ui.screen.onboarding.OnboardingRoute
import com.pascal.noctra.ui.screen.settings.SettingsRoute
import com.pascal.noctra.ui.screen.splash.SplashRoute
import com.russhwolf.settings.Settings

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
                BottomBar(navController)
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
