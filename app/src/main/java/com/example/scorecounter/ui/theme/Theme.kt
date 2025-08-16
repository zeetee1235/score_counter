package com.example.scorecounter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF006494),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFBFE4FF),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF001E30),
    secondary = androidx.compose.ui.graphics.Color(0xFF4F378B),
    onSecondary = androidx.compose.ui.graphics.Color.White,
    background = androidx.compose.ui.graphics.Color(0xFFF7FBFF),
    onBackground = androidx.compose.ui.graphics.Color(0xFF111416),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onSurface = androidx.compose.ui.graphics.Color(0xFF111416),
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF66C1FF),
    onPrimary = androidx.compose.ui.graphics.Color(0xFF00344F),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF004B70),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFBFE4FF),
    secondary = androidx.compose.ui.graphics.Color(0xFFD0BCFF),
    onSecondary = androidx.compose.ui.graphics.Color(0xFF381E72),
    background = androidx.compose.ui.graphics.Color(0xFF0B0F12),
    onBackground = androidx.compose.ui.graphics.Color(0xFFE2E7EC),
    surface = androidx.compose.ui.graphics.Color(0xFF111416),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE2E7EC),
)

@Composable
fun ScoreCounterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
