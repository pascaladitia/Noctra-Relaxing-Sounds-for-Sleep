package com.pascal.noctra.utils

import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun localizedMonthName(month: Int): String {
    val formatter = NSDateFormatter()
    formatter.locale = NSLocale.currentLocale
    return formatter.standaloneMonthSymbols[month - 1] as String
}

actual fun localizedWeekdayNames(): List<String> {
    val formatter = NSDateFormatter()
    formatter.locale = NSLocale.currentLocale
    val symbols = formatter.shortStandaloneWeekdaySymbols
    return listOf(
        symbols[0] as String,
        symbols[1] as String,
        symbols[2] as String,
        symbols[3] as String,
        symbols[4] as String,
        symbols[5] as String,
        symbols[6] as String,
    )
}