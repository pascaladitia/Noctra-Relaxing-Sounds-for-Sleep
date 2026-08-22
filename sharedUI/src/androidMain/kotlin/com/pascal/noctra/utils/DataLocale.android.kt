package com.pascal.noctra.utils

import java.text.DateFormatSymbols
import java.util.Locale

actual fun localizedMonthName(month: Int): String {
    val symbols = DateFormatSymbols.getInstance(Locale.getDefault())
    return symbols.months[month - 1]
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

actual fun localizedWeekdayNames(): List<String> {
    val symbols = DateFormatSymbols.getInstance(Locale.getDefault())
    val short = symbols.shortWeekdays
    return listOf(
        short[1],
        short[2],
        short[3],
        short[4],
        short[5],
        short[6],
        short[7]
    ).map { it.replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase(Locale.getDefault()) else c.toString() } }
}