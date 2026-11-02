package com.example.fittrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    background = LightBackground,
    onBackground = LightText,
    surface = LightCard,
    onSurface = LightText,
    onSurfaceVariant = LightGray,
    surfaceVariant = LightButton,
    outline = LightTrack,
    error = Red
)

private val DarkColors = darkColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkCard,
    onSurface = DarkText,
    onSurfaceVariant = DarkGray,
    surfaceVariant = DarkButton,
    outline = DarkButton,
    error = Red
)

@Composable
fun FitTrackTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
