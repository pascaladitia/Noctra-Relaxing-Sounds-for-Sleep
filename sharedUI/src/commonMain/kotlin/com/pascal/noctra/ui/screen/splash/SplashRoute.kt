package com.pascal.noctra.ui.screen.splash

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onTimeout: (Boolean) -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(500)
        onTimeout(true)
    }

    SplashScreen(
        modifier = modifier.padding(paddingValues)
    )
}

