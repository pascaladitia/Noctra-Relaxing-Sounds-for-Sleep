package com.pascal.noctra.ui.screen.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pascal.noctra.ui.screen.onboarding.state.LocalOnboardingEvent
import com.pascal.noctra.ui.screen.onboarding.state.OnboardingUiState
import com.pascal.noctra.ui.theme.NocturneAccent
import com.pascal.noctra.ui.theme.NocturneTextMuted
import kotlinx.coroutines.launch

data class OnboardingPage(
    val icon: String,
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPage(
        icon = "\uD83C\uDF19",
        title = "Sleep Better",
        description = "Discover a world of calming sounds designed to help you fall asleep faster and wake up refreshed."
    ),
    OnboardingPage(
        icon = "\uD83C\uDFB6",
        title = "Mix Your Sounds",
        description = "Create your perfect soundscape by layering multiple sounds with independent volume controls."
    ),
    OnboardingPage(
        icon = "\uD83D\uDCA4",
        title = "Sleep Anywhere",
        description = "Sounds play seamlessly in the background and from the lock screen. Set a timer and drift off."
    )
)

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState = OnboardingUiState()
) {
    val event = LocalOnboardingEvent.current
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val data = onboardingPages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = data.icon, style = MaterialTheme.typography.displayLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = data.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = NocturneTextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(onboardingPages.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (pagerState.currentPage == index) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (pagerState.currentPage == index) NocturneAccent
                                else NocturneTextMuted.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        event.onComplete()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NocturneAccent),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (pagerState.currentPage < onboardingPages.size - 1) "Next" else "Get Started",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
