package com.example.monlauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun MonLauncherTheme(
    darkTheme: Boolean,
    largeText: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            background = Color.Black,
            surface = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColorScheme()
    }

    val base = Typography()
    val typography = if (largeText) {
        Typography(
            bodyLarge = base.bodyLarge.copy(fontSize = 20.sp),
            titleMedium = base.titleMedium.copy(fontSize = 22.sp),
            headlineMedium = base.headlineMedium.copy(fontSize = 28.sp)
        )
    } else base

    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
