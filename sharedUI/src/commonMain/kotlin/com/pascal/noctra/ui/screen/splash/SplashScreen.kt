package com.pascal.noctra.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctra.sharedui.generated.resources.Res
import noctra.sharedui.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(120.dp)
        )
    }
}

