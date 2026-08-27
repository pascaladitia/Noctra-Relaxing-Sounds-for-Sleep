package com.pascal.noctra.domain.model.timer

data class SleepTimerState(
    val isActive: Boolean = false,
    val totalDurationMs: Long = 0L,
    val remainingMs: Long = 0L,
    val fadeOutDurationMs: Long = 30_000L
) {
    val remainingMinutes: Int get() = (remainingMs / 60_000).toInt()
    val remainingSeconds: Int get() = ((remainingMs % 60_000) / 1000).toInt()
    val progress: Float get() = if (totalDurationMs > 0) remainingMs.toFloat() / totalDurationMs else 0f
    val displayTime: String get() {
        val hours = remainingMinutes / 60
        val mins = remainingMinutes % 60
        val secs = remainingSeconds
        return if (hours > 0) "${hours}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
        else "${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
    }
}
