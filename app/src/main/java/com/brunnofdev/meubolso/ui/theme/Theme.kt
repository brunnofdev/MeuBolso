
package com.brunnofdev.meubolso.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue,
    onPrimary = DarkBlue,
    secondary = SoftBlue,
    onSecondary = DarkText,
    tertiary = IncomeGreen,
    background = BackgroundDark,
    onBackground = LightText,
    surface = SurfaceDark,
    onSurface = LightText
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DarkBlue,
    secondary = SoftBlue,
    onSecondary = DarkText,
    tertiary = DarkBlue,
    background = BackgroundBlue,
    onBackground = DarkText,
    surface = White,
    onSurface = DarkText,
    error = ExpenseRed,
    onError = White
)

@Composable
fun MeuBolsoTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
