package com.pascal.noctra.utils

enum class ScreenOrientation {
    LANDSCAPE,
    PORTRAIT
}

expect fun setScreenOrientation(orientation: ScreenOrientation)
