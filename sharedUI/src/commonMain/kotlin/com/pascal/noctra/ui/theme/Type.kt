package com.pascal.noctra.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import noctra.sharedui.generated.resources.Res
import noctra.sharedui.generated.resources.roboto
import noctra.sharedui.generated.resources.roboto_bold
import org.jetbrains.compose.resources.Font

@Composable
fun getTypography(fontScale: Float = 1f): Typography {
    return Typography(
        headlineLarge = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto_bold)),
            fontSize = (34 * fontScale).sp,
            lineHeight = (36 * fontScale).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto_bold)),
            fontSize = (28 * fontScale).sp,
            lineHeight = (32 * fontScale).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto)),
            fontWeight = FontWeight.Bold,
            fontSize = (18 * fontScale).sp,
            lineHeight = (26 * fontScale).sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto)),
            fontWeight = FontWeight.Bold,
            fontSize = (18 * fontScale).sp,
            lineHeight = (24 * fontScale).sp
        ),
        titleMedium = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto)),
            fontWeight = FontWeight.Bold,
            fontSize = (16 * fontScale).sp,
            lineHeight = (22 * fontScale).sp
        ),
        titleSmall = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto)),
            fontWeight = FontWeight.Bold,
            fontSize = (14 * fontScale).sp,
            lineHeight = (20 * fontScale).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto)),
            fontWeight = FontWeight.Normal,
            fontSize = (18 * fontScale).sp,
            lineHeight = (26 * fontScale).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto)),
            fontWeight = FontWeight.Normal,
            fontSize = (16 * fontScale).sp,
            lineHeight = (24 * fontScale).sp
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily(Font(Res.font.roboto)),
            fontWeight = FontWeight.Normal,
            fontSize = (14 * fontScale).sp,
            lineHeight = (20 * fontScale).sp
        ),
    )
}
