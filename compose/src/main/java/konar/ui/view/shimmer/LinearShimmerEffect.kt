package konar.ui.view.shimmer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.IntSize
import konar.ui.view.theme.ShimmerTheme
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * A shimmer effect that displays a linear gradient.
 *
 * @param durationMillis The duration of the shimmer animation in milliseconds.
 * @param tilt The tilt of the shimmer effect in degrees.
 */
class LinearShimmerEffect(
    override val durationMillis: Int = 800,
    private val tilt: Float = 45f,
) : ShimmerEffect {
    override fun brush(
        progress: Float,
        size: IntSize,
        theme: ShimmerTheme,
    ): Brush {
        val width = size.width.toFloat().coerceAtLeast(1f)
        val height = size.height.toFloat().coerceAtLeast(1f)

        val maxDim = max(width, height)

        val angleRad = Math.toRadians(tilt.toDouble())
        val dx = cos(angleRad).toFloat()
        val dy = sin(angleRad).toFloat()

        val offset = maxDim * progress
        val start = Offset(offset * dx - SHIMMER_WIDTH, offset - SHIMMER_WIDTH * dy)
        val end = Offset(start.x + SHIMMER_WIDTH * dx, start.y + SHIMMER_WIDTH * dy)

        val colorStops =
            listOf(
                theme.baseColor.copy(alpha = theme.opacity),
                theme.highlightColor,
                theme.baseColor.copy(alpha = theme.opacity),
            )

        return Brush.linearGradient(
            colors = colorStops,
            start = start,
            end = end,
        )
    }

    companion object {
        private const val SHIMMER_WIDTH = 200f
    }
}
