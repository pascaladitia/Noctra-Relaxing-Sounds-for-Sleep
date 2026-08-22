package com.pascal.noctra.utils.admob

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect fun initializeAdMob()

expect class InterstitialAdManager {
    fun load(adUnitId: String)
    fun show(): Boolean
    fun destroy()
}

expect class AppOpenAdManager {
    fun load(adUnitId: String)
    fun show(): Boolean
    fun destroy()
}

@Composable
expect fun BannerAdView(adUnitId: String, modifier: Modifier = Modifier)
