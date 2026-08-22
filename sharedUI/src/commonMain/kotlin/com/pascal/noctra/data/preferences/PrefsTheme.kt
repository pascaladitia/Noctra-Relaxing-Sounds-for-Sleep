package com.pascal.noctra.data.preferences

import com.pascal.noctra.createSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object PrefsTheme {

    private const val THEME_MODE_KEY = "theme_mode"
    private const val LANGUAGE_KEY = "language"
    private const val FONT_SCALE_KEY = "font_scale"
    private const val DEFAULT_MODE = "SYSTEM"
    private const val DEFAULT_LANGUAGE = "ENGLISH"
    private const val DEFAULT_FONT_SCALE = "1.0"

    private val settings: Settings? by lazy {
        try {
            createSettings()
        } catch (e: Throwable) {
            null
        }
    }

    private val _themeFlow = MutableStateFlow(getThemeMode())
    private val _languageFlow = MutableStateFlow(getLanguage())
    private val _fontScaleFlow = MutableStateFlow(getFontScale())
    val themeFlow: Flow<String> = _themeFlow
    val languageFlow: Flow<String> = _languageFlow
    val fontScaleFlow: Flow<String> = _fontScaleFlow

    fun observeThemeMode(): Flow<String> = themeFlow
    fun observeLanguage(): Flow<String> = languageFlow
    fun observeFontScale(): Flow<String> = fontScaleFlow

    fun saveThemeMode(mode: String) {
        settings?.let { it[THEME_MODE_KEY] = mode }
        _themeFlow.value = mode
    }

    fun saveLanguage(language: String) {
        settings?.let { it[LANGUAGE_KEY] = language }
        _languageFlow.value = language
    }

    fun saveFontScale(scale: Float) {
        val value = scale.toString()
        settings?.let { it[FONT_SCALE_KEY] = value }
        _fontScaleFlow.value = value
    }

    fun getThemeMode(): String {
        return settings?.get<String>(THEME_MODE_KEY) ?: DEFAULT_MODE
    }

    fun getLanguage(): String {
        return settings?.get<String>(LANGUAGE_KEY) ?: DEFAULT_LANGUAGE
    }

    fun getFontScale(): String {
        return settings?.get<String>(FONT_SCALE_KEY) ?: DEFAULT_FONT_SCALE
    }

    fun clear() {
        settings?.clear()
        _themeFlow.value = DEFAULT_MODE
        _languageFlow.value = DEFAULT_LANGUAGE
        _fontScaleFlow.value = DEFAULT_FONT_SCALE
    }
}
