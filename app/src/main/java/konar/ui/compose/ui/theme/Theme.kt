package konar.ui.compose.ui.theme

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
import konar.ui.view.shimmer.LinearShimmerEffect
import konar.ui.view.theme.ShimmerTheme
import konar.ui.view.theme.ShimmerThemeProvider

private val DarkColorScheme =
    darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40,
        background = OffWhite,
        surface = OffWhite,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Dark,
        onSurface = Dark,
    )

@Composable
fun ExampleAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    val shimmerTheme =
        ShimmerTheme(
            effect =
                LinearShimmerEffect(
                    shimmerWidth = 800f,
                ),
            baseColor = colorScheme.surface,
            highlightColor = colorScheme.surfaceVariant,
            opacity = 1f,
        )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        ShimmerThemeProvider(theme = shimmerTheme) {
            content()
        }
    }
}
