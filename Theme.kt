package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = ExpressGreenLight,
  onPrimary = Color.Black,
  primaryContainer = ExpressGreenDark,
  onPrimaryContainer = ExpressGreenContainer,
  secondary = ExpressOrangeLight,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF5C2600),
  onSecondaryContainer = ExpressOrangeContainer,
  background = SurfaceDark,
  surface = CardDark,
  onBackground = TextPrimaryDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = Color(0xFF334155),
  onSurfaceVariant = TextSecondaryDark,
  outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
  primary = ExpressGreenPrimary,
  onPrimary = Color.White,
  primaryContainer = ExpressGreenContainer,
  onPrimaryContainer = ExpressGreenDark,
  secondary = ExpressOrangeAccent,
  onSecondary = Color.White,
  secondaryContainer = ExpressOrangeContainer,
  onSecondaryContainer = Color(0xFF7A2E00),
  background = SurfaceLight,
  surface = CardLight,
  onBackground = TextPrimaryLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = Color(0xFFF1F5F9),
  onSurfaceVariant = TextSecondaryLight,
  outline = Color(0xFFE2E8F0)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our brand colors consistently
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

