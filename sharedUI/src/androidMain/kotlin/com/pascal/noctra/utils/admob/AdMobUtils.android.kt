package com.pascal.noctra.utils.admob

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pascal.noctra.ContextUtils

actual fun initializeAdMob() {
    val context = ContextUtils.context
    MobileAds.initialize(context) {}
}

actual class InterstitialAdManager {
    private var interstitialAd: InterstitialAd? = null

    actual fun load(adUnitId: String) {
        val context = ContextUtils.context
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                interstitialAd = null
            }
        })
    }

    actual fun show(): Boolean {
        val activity = ContextUtils.activity
        val ad = interstitialAd ?: return false
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                interstitialAd = null
            }
        }
        ad.show(activity)
        return true
    }

    actual fun destroy() {
        interstitialAd = null
    }
}

actual class AppOpenAdManager {
    private var appOpenAd: AppOpenAd? = null

    actual fun load(adUnitId: String) {
        val context = ContextUtils.context
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(context, adUnitId, adRequest, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                appOpenAd = null
            }
        })
    }

    actual fun show(): Boolean {
        val activity = ContextUtils.activity
        val ad = appOpenAd ?: return false
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
            }
        }
        ad.show(activity)
        return true
    }

    actual fun destroy() {
        appOpenAd = null
    }
}

@SuppressLint("MissingPermission")
@Composable
actual fun BannerAdView(adUnitId: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = {
            AdView(it).apply {
                setAdSize(AdSize.BANNER)
                setAdUnitId(adUnitId)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
