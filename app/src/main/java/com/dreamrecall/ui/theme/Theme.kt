package com.dreamrecall.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PurpleBlueAccent,
    secondary = DarkPurpleAccent,
    tertiary = PurpleBlueAccent,
    background = BlackBackground,
    surface = CardBackground,
    onPrimary = SoftWhiteText,
    onSecondary = SoftWhiteText,
    onTertiary = SoftWhiteText,
    onBackground = SoftWhiteText,
    onSurface = SoftWhiteText,
)

// Subtle ripple effect matching the "Magic UI" requirement
private object SmoothRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = PurpleBlueAccent

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color(0xFF6A00FF),
        lightTheme = !isSystemInDarkTheme()
    )
}

@Composable
fun DreamRecallTheme(
    darkTheme: Boolean = true, // Force dark mode as per requirements
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        CompositionLocalProvider(LocalRippleTheme provides SmoothRippleTheme) {
            content()
        }
    }
}
