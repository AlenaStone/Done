package com.done.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = Teal80,
    tertiary = Coral80,
    background = DoneText,
    surface = Color(0xFF2C3140),
    surfaceVariant = Color(0xFF424A5F),
    onPrimary = Color(0xFF10213F),
    onSecondary = Color(0xFF063A35),
    onTertiary = Color(0xFF4A1B14),
    onBackground = Color(0xFFF5F7FA),
    onSurface = Color(0xFFF5F7FA),
    onSurfaceVariant = Color(0xFFC9CED8)
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = Teal40,
    tertiary = Coral40,
    background = DoneBackground,
    surface = DoneSurface,
    surfaceVariant = DoneSurfaceVariant,
    onPrimary = DoneSurface,
    onSecondary = DoneSurface,
    onTertiary = DoneSurface,
    onBackground = DoneText,
    onSurface = DoneText,
    onSurfaceVariant = DoneMutedText
)

@Composable
fun DoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
