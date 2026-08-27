package com.pascal.noctra.utils

import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppInfo {
    val versionName: String
    val versionCode: String
    val appId: String
}

expect fun showToast(msg: String)

expect fun actionShareUrl(url: String?)

expect fun openAppSettings()

expect fun downloadDirectory(): String

expect fun isOnline(): Boolean

expect suspend fun decodeByteArrayToImageBitmap(bytes: ByteArray): ImageBitmap?

fun getGreeting(): String {
    val now = kotlin.time.Clock.System.now()
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = localDateTime.hour
    return when {
        hour in 5..11 -> "Good Morning"
        hour in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

@OptIn(ExperimentalUuidApi::class)
fun generateUID(): String {
    return Uuid.random().toString()
}

fun getCurrentDateTimeString(): String {
    return kotlin.time.Clock.System.now().toString()
}

fun String.extractChapterSlug(): String {
    return this
        .trimEnd('/')
        .substringAfterLast('/')
}

fun String.extractResolution(): String {
    val regex = "\\d+p".toRegex()
    val matches = regex.findAll(this).toList()
    return if (matches.isNotEmpty()) {
        matches.joinToString(", ") { it.value }
    } else {
        this
    }
}

fun String.toEnglishDate(): String {
    val parts = this.split("-")
    if (parts.size != 3) return this

    val year = parts[0]
    val month = parts[1].toIntOrNull() ?: return this
    val day = parts[2]

    val monthName = month.toEnglishMonthName()

    return "$day $monthName $year"
}

fun Int.toEnglishMonthName(): String {
    return when (this) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> ""
    }
}

fun setMandatoryTitle(title: String?, isMandatory: Boolean = true): AnnotatedString {
    val safeTitle = title.orEmpty()

    return buildAnnotatedString {
        append(safeTitle)
        if (isMandatory) {
            withStyle(style = SpanStyle(color = Red)) {
                append("*")
            }
        }
    }
}