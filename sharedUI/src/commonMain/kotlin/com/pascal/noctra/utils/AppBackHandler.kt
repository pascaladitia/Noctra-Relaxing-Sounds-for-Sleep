package com.pascal.noctra.utils

import androidx.compose.runtime.Composable

@Composable
expect fun AppBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
)