package com.pascal.noctra.ui.navigation.screen

sealed class BaseScreen(val route: String) {
    data object SplashScreen : BaseScreen("splash")
    data object LoginScreen : BaseScreen("login")
    data object OnboardingScreen : BaseScreen("onboarding")
    data object HomeScreen : BaseScreen("home")
    data object MixerScreen : BaseScreen("mixer")
    data object SettingsScreen : BaseScreen("settings")
}
