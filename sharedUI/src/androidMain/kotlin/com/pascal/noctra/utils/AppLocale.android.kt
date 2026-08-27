package com.pascal.noctra.utils

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class AndroidAppLocaleManager(
    private val context: Context,
) : AppLocaleManager {
    override fun getLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(Context.LOCALE_SERVICE) as? android.app.LocaleManager
            val locales = localeManager?.applicationLocales
            if (locales == null || locales.isEmpty) {
                "en"
            } else {
                locales[0]?.toLanguageTag()?.split("-")?.firstOrNull() ?: "en"
            }
        } else {
            AppCompatDelegate.getApplicationLocales()
                .toLanguageTags()
                .split("-")
                .firstOrNull() ?: "en"
        }
    }
}

@Composable
actual fun rememberAppLocale(): AppLang {
    val configuration = LocalConfiguration.current
    val localeCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.locales[0]?.toLanguageTag()?.split("-")?.firstOrNull() ?: "en"
    } else {
        configuration.locale.language ?: "en"
    }
    return remember(localeCode) { localeCode.toApLang() }
}

actual fun setAppLanguage(language: String) {
    val localeTag = when (language) {
        "INDONESIA" -> "id"
        else -> "en"
    }
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(localeTag)
    )
}