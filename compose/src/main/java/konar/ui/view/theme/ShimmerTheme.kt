package konar.ui.view.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import konar.ui.view.shimmer.LinearShimmerEffect
import konar.ui.view.shimmer.ShimmerEffect

/**
 * Defines the appearance of the shimmer effect.
 *
 * @param effect The shimmer effect, see [ShimmerEffect].
 * @param baseColor The base color of the shimmer.
 * @param highlightColor The highlight color of the shimmer.
 */
data class ShimmerTheme(
    val effect: ShimmerEffect = LinearShimmerEffect(),
    val baseColor: Color,
    val highlightColor: Color,
    val opacity: Float,
)

/**
 * CompositionLocal used to pass [ShimmerTheme] down the composable tree.
 */
internal val LocalShimmerTheme =
    compositionLocalOf {
        ShimmerTheme(
            baseColor = Color.LightGray,
            highlightColor = Color.White,
            opacity = 1f,
        )
    }

/**
 * Provides a [ShimmerTheme] to the composable tree.
 *
 * @param theme The shimmer theme to provide.
 * @param content The content to be rendered with the provided theme.
 */
@Composable
fun ShimmerThemeProvider(
    theme: ShimmerTheme,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalShimmerTheme provides theme,
        content = content,
    )
}
