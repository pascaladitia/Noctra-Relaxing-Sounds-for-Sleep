package com.pascal.noctra.utils

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import noctra.sharedui.generated.resources.Res
import noctra.sharedui.generated.resources.label_language_en
import noctra.sharedui.generated.resources.label_language_indonesian
import org.jetbrains.compose.resources.StringResource

enum class AppLang(
    val code: String,
    val stringRes: StringResource
) {
    ENGLISH("en", Res.string.label_language_en),
    INDONESIA("id", Res.string.label_language_indonesian);

    companion object {
        fun fromCode(code: String): AppLang {
            return when (code) {
                "id", "in", "INDONESIA" -> INDONESIA
                else -> ENGLISH
            }
        }
    }
}

enum class AppLanguage { ENGLISH, INDONESIA }

@Stable
val LocalAppLocalization = compositionLocalOf { AppLang.ENGLISH }

fun String.toApLang(): AppLang = AppLang.fromCode(this)