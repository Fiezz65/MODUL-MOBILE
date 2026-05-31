package com.example.modul5compose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    background = Color.Black,
    surface = Color(0xFF121212),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun Modul5Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}