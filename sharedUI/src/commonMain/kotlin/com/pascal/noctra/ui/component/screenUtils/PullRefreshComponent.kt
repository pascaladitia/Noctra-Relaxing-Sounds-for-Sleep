package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRefreshComponent(
    modifier: Modifier = Modifier,
    onRefresh: suspend () -> Unit,
    content: @Composable () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val refreshJob = remember { mutableStateOf<Job?>(null) }

    fun triggerRefresh() {
        refreshJob.value?.cancel()
        refreshJob.value = coroutineScope.launch {
            isRefreshing = true
            delay(500)
            onRefresh()
            isRefreshing = false
        }
    }

    PullToRefreshBox(
        modifier = modifier,
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = { triggerRefresh() },
        indicator = {
            if (refreshState.distanceFraction > 0f || isRefreshing) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .graphicsLayer {
                            translationY = refreshState.distanceFraction * 100
                        }
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                }
            }
        }
    ) {
        content()
    }
}