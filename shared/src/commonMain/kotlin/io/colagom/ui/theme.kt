package io.colagom.ui

import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val colorPalette = darkColors(
    background = Color.Transparent,
    primary = Color.LightGray,
    onSurface = Color.LightGray,
    onBackground = Color.LightGray,
    surface = Color(0xFF121212),
    error = Color.Red
)

@Composable
fun Theme(content: @Composable () -> Unit) {
    androidx.compose.material.MaterialTheme(
        colors = colorPalette,
        content = content
    )
}