package com.cc221045.mathemelloccl3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily


import androidx.compose.material3.Button

import androidx.compose.material3.Text


import androidx.compose.material3.ButtonDefaults

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer




private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1565C0),
    secondary = Color(0xFF00ACC1),
    tertiary = Color(0xFF4DD0E1)

)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF42A5F5),
    secondary = Color(0xFF80DEEA),
    tertiary = Color(0xFF00ACC1)

)

val KotloTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    )

)

@Composable
fun KotloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KotloTypography,
        content = content
    )
}












@Composable
fun AnimatedButton(text: String, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 8.dp,
        animationSpec = tween(durationMillis = 200)
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSystemInDarkTheme()) Color(0xFF455A64) else Color(0xFF78909C),
        animationSpec = tween(durationMillis = 300)
    )

    Button(
        onClick = { onClick() },
        modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }

    DisposableEffect(isPressed) {
        onDispose { isPressed = false }
    }
}


