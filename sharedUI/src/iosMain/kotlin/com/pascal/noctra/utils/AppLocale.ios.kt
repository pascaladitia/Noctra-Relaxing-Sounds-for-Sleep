package com.pascal.noctra.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

class IosAppLocaleManager : AppLocaleManager {
    override fun getLocale(): String {
        return NSLocale.currentLocale.languageCode ?: "en"
    }
}

@Composable
actual fun rememberAppLocale(): AppLang {
    val locale = IosAppLocaleManager().getLocale()
    return remember(locale) { locale.toApLang() }
}

actual fun setAppLanguage(language: String) {
    val localeIdentifier = when (language) {
        "INDONESIA" -> "id"
        else -> "en"
    }
    platform.Foundation.NSUserDefaults.standardUserDefaults.setObject(
        listOf(localeIdentifier),
        forKey = "AppleLanguages"
    )
}