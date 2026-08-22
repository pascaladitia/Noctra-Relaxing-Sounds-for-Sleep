package com.pascal.noctra.utils.admob

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.GoogleMobileAds.GADAppOpenAd
import cocoapods.GoogleMobileAds.GADBannerView
import cocoapods.GoogleMobileAds.GADInterstitialAd
import cocoapods.GoogleMobileAds.GADRequest
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication

actual fun initializeAdMob() {
    // Handled via app delegate or Info.plist GADApplicationIdentifier
}

actual class InterstitialAdManager {
    private var interstitialAd: GADInterstitialAd? = null

    actual fun load(adUnitId: String) {
        GADInterstitialAd.loadWithAdUnitID(
            adUnitId = adUnitId,
            request = GADRequest.request(),
            completionHandler = { ad, error ->
                if (error == null) {
                    interstitialAd = ad
                }
            }
        )
    }

    actual fun show(): Boolean {
        val ad = interstitialAd ?: return false
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return false
        ad.presentFromRootViewController(rootViewController)
        return true
    }

    actual fun destroy() {
        interstitialAd = null
    }
}

actual class AppOpenAdManager {
    private var appOpenAd: GADAppOpenAd? = null

    actual fun load(adUnitId: String) {
        GADAppOpenAd.loadWithAdUnitID(
            adUnitId = adUnitId,
            request = GADRequest.request(),
            orientation = 0,
            completionHandler = { ad, error ->
                if (error == null) {
                    appOpenAd = ad
                }
            }
        )
    }

    actual fun show(): Boolean {
        val ad = appOpenAd ?: return false
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return false
        ad.presentFromRootViewController(rootViewController)
        return true
    }

    actual fun destroy() {
        appOpenAd = null
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun BannerAdView(adUnitId: String, modifier: Modifier) {
    UIKitView(
        modifier = modifier,
        factory = {
            GADBannerView().apply {
                adUnitID = adUnitId
                rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
                loadRequest(GADRequest.request())
            }
        }
    )
}
