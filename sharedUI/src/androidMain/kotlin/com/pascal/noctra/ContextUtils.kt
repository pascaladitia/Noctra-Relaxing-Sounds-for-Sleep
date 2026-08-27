package com.pascal.noctra

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ContextUtils {

    private var kmpApplicationContext: Context? = null
    private var currentActivity: Activity? = null

    val context: Context
        get() = kmpApplicationContext
            ?: error("Android context has not been set. Call setContext() in Application.onCreate()")

    val activity: Activity
        get() = currentActivity
            ?: error("Activity has not been set. Call setActivity() in Activity.onCreate()")


    fun setActivity(activity: Activity?) {
        currentActivity = activity
        kmpApplicationContext = activity?.applicationContext
    }

    fun clearActivity(activity: Activity) {
        if (currentActivity === activity) {
            currentActivity = null
        }
    }
}
