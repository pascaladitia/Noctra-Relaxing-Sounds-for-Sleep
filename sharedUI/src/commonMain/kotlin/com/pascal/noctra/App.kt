package com.pascal.noctra

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.RoomDatabase
import com.pascal.noctra.data.local.database.AppDatabase
import com.pascal.noctra.ui.navigation.route.NavBaseRoute
import com.pascal.noctra.ui.theme.AppTheme
import com.pascal.noctra.utils.LocalAppLocalization
import com.pascal.noctra.utils.rememberAppLocale
import com.russhwolf.settings.Settings

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    CompositionLocalProvider(LocalAppLocalization provides rememberAppLocale()) {
        NavBaseRoute()
    }
}

expect fun createSettings(): Settings

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
