package com.pascal.noctra.ui.component.screenUtils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pascal.noctra.ui.component.button.ButtonComponent
import com.pascal.noctra.utils.localizedMonthName
import com.pascal.noctra.utils.localizedWeekdayNames
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit,
    backDate: Boolean = false,
    isRange: Boolean = false,
    maxDate: Int = 0
) {
    if (!show) return

    val today = remember {
        kotlin.time.Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }

    val minDate = if (backDate) today.minus(3650, DateTimeUnit.DAY) else today
    val maxAllowedDate = if (maxDate == 0) today.plus(3650, DateTimeUnit.DAY)
    else today.plus(maxDate, DateTimeUnit.DAY)

    var selectedStart by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEnd by remember { mutableStateOf<LocalDate?>(null) }

    var viewYear by remember { mutableIntStateOf(today.year) }
    var viewMonth by remember { mutableIntStateOf(today.monthNumber) }
    var slideForward by remember { mutableStateOf(true) }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = if (isRange) "Select Date Range" else "Select Date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            SelectedDateRow(selectedStart, selectedEnd, isRange)

            Spacer(Modifier.height(8.dp))

            MonthNavigationBar(
                year = viewYear,
                month = viewMonth,
                onPrev = {
                    slideForward = false
                    if (viewMonth == 1) {
                        viewMonth = 12; viewYear--
                    } else viewMonth--
                },
                onNext = {
                    slideForward = true
                    if (viewMonth == 12) {
                        viewMonth = 1; viewYear++
                    } else viewMonth++
                }
            )

            Spacer(Modifier.height(8.dp))

            WeekdayHeader()

            Spacer(Modifier.height(4.dp))

            AnimatedContent(
                targetState = viewYear to viewMonth,
                transitionSpec = {
                    if (slideForward) {
                        (slideInHorizontally(tween(280)) { it } + fadeIn(tween(180))) togetherWith
                                (slideOutHorizontally(tween(280)) { -it } + fadeOut(tween(180)))
                    } else {
                        (slideInHorizontally(tween(280)) { -it } + fadeIn(tween(180))) togetherWith
                                (slideOutHorizontally(tween(280)) { it } + fadeOut(tween(180)))
                    }
                },
                label = "calendarSlide"
            ) { (year, month) ->
                CalendarGrid(
                    year = year,
                    month = month,
                    today = today,
                    minDate = minDate,
                    maxDate = maxAllowedDate,
                    selectedStart = selectedStart,
                    selectedEnd = selectedEnd,
                    isRange = isRange,
                    onDateClick = { date ->
                        if (!isRange) {
                            selectedStart = date
                        } else {
                            when {
                                selectedStart == null -> selectedStart = date
                                selectedEnd == null -> {
                                    if (date >= selectedStart!!) selectedEnd = date
                                    else {
                                        selectedStart = date; selectedEnd = null
                                    }
                                }

                                else -> {
                                    selectedStart = date; selectedEnd = null
                                }
                            }
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = "Confirm",
                enabled = selectedStart != null,
                onClick = {
                    selectedStart?.let { start ->
                        onConfirm(formatDate(start), selectedEnd?.let { formatDate(it) })
                    }
                }
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SelectedDateRow(
    start: LocalDate?,
    end: LocalDate?,
    isRange: Boolean
) {
    val startText = start?.let { formatDateFull(it) } ?: "—"
    val endText = end?.let { formatDateFull(it) } ?: "—"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = if (isRange) Arrangement.SpaceBetween else Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isRange) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "From",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = startText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "→",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "To",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = endText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            Text(
                text = startText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun MonthNavigationBar(
    year: Int, month: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrev) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "${localizedMonthName(month)} $year",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(onClick = onNext) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun WeekdayHeader() {
    val days = remember { localizedWeekdayNames() }
    Row(Modifier.fillMaxWidth()) {
        days.forEach { label ->
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    year: Int,
    month: Int,
    today: LocalDate,
    minDate: LocalDate,
    maxDate: LocalDate,
    selectedStart: LocalDate?,
    selectedEnd: LocalDate?,
    isRange: Boolean,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDay = LocalDate(year, month, 1)
    val startOffset = (firstDay.dayOfWeek.ordinal + 1) % 7
    val daysInMonth = daysInMonth(year, month)

    val cells: List<LocalDate?> = buildList {
        repeat(startOffset) { add(null) }
        for (d in 1..daysInMonth) add(LocalDate(year, month, d))
        val total = startOffset + daysInMonth
        val remainder = if (total % 7 == 0) 0 else 7 - (total % 7)
        repeat(remainder) { add(null) }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        cells.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Box(modifier = Modifier.weight(1f)) {
                        if (date != null) {
                            DayCell(
                                date = date,
                                today = today,
                                minDate = minDate,
                                maxDate = maxDate,
                                selectedStart = selectedStart,
                                selectedEnd = selectedEnd,
                                isRange = isRange,
                                onClick = { onDateClick(date) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    today: LocalDate,
    minDate: LocalDate,
    maxDate: LocalDate,
    selectedStart: LocalDate?,
    selectedEnd: LocalDate?,
    isRange: Boolean,
    onClick: () -> Unit
) {
    val isDisabled = date !in minDate..maxDate
    val isToday = date == today
    val isSelected = date == selectedStart || date == selectedEnd

    val inRange = isRange &&
            selectedStart != null &&
            selectedEnd != null &&
            date > selectedStart &&
            date < selectedEnd

    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant
    val onSurface = MaterialTheme.colorScheme.onSurface

    val bgColor by animateColorAsState(
        targetValue = when {
            isSelected -> primary
            inRange -> primaryContainer
            else -> Color.Transparent
        },
        animationSpec = tween(150),
        label = "dayBg"
    )

    val textColor = when {
        isDisabled -> outlineVariant
        isSelected -> onPrimary
        inRange -> primary
        else -> onSurface
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (isToday && !isSelected)
                    Modifier.border(1.5.dp, primary, CircleShape)
                else Modifier
            )
            .then(
                if (!isDisabled) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.day.toString(),
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

fun formatDate(date: LocalDate): String {
    val day = date.day.toString().padStart(2, '0')
    val month = date.month.number.toString().padStart(2, '0')
    val year = (date.year % 100).toString().padStart(2, '0')
    return "$day/$month/$year"
}

private fun formatDateFull(date: LocalDate): String {
    val day = date.day.toString().padStart(2, '0')
    return "$day ${localizedMonthName(date.month.number)} ${date.year}"
}

private fun daysInMonth(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) 29 else 28
    else -> 30
}