package com.cc221045.mathemelloccl3.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.sp
import com.cc221045.mathemelloccl3.R


val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

)

val appFontFamily = FontFamily(
    Font(R.font.thin, FontWeight.Thin),
    Font(R.font.light, FontWeight.Light),
    Font(R.font.regular, FontWeight.Normal),
    Font(R.font.medium, FontWeight.Medium),
    Font(R.font.bold, FontWeight.Bold),
    Font(R.font.extrabold, FontWeight.ExtraBold),
    Font(R.font.black, FontWeight.Black)
)