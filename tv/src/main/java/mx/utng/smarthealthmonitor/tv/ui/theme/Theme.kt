package mx.utng.smarthealthmonitor.tv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1B4F8A),
    secondary = Color(0xFFD4860A),
    background = Color(0xFF0D1117),
    surface = Color(0xFF161B22),
    error = Color(0xFFB3261E),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1B4F8A),
    secondary = Color(0xFFD4860A),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB3261E),
)

@Composable
fun SmartHealthTvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
