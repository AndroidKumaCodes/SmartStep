package com.buchwald.smartstep.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Mapping custom colors to Light Scheme
private val LightColorScheme = lightColorScheme(
    // Brand / buttons
    primary = ButtonPrimary,
    onPrimary = TextWhite,

    secondary = ButtonSecondary,
    onSecondary = TextPrimary,

// Use tertiary as a neutral UI fill (e.g., wheel highlight band)
    tertiary = BackgroundTertiary,
    onTertiary = TextPrimary,

// App backgrounds
    background = BackgroundMain,
    onBackground = TextPrimary,

// Surfaces (cards, dialogs, sheets)
    surface = BackgroundWhite,
    onSurface = TextPrimary,

// Slightly tinted/alternative surface (cards on top of background, subtle sections)
    surfaceVariant = BackgroundSecondary,
    onSurfaceVariant = TextSecondary,

// Borders / dividers
    outline = StrokeMain,

// Overlays / scrims (your “White 20%” token)
    scrim = BackgroundWhite20,
)

// Designer does not specify dark mode; making it identical to Light
private val DarkColorScheme = LightColorScheme

@Composable
fun SmartStepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
        content = content,
    )
}
