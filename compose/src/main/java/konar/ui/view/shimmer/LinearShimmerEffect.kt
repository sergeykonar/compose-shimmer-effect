package konar.ui.view.shimmer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.IntSize
import konar.ui.view.state.ShimmerDirection
import konar.ui.view.theme.ShimmerTheme
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * A shimmer effect that displays a linear gradient.
 *
 * @param durationMillis The duration of the shimmer animation in milliseconds.
 * @param direction The direction of the shimmer animation.
 * @param shimmerWidth The width of the shimmer highlight.
 */
class LinearShimmerEffect(
    override val durationMillis: Int = 800,
    private val direction: ShimmerDirection = ShimmerDirection.FromTopLeftToBottomRight,
    private val shimmerWidth: Float = SHIMMER_WIDTH,
) : ShimmerEffect {
    private val angleRad =
        Math.toRadians(
            when (direction) {
                ShimmerDirection.FromTopLeftToBottomRight -> DIAGONAL_45
                ShimmerDirection.FromTopRightToBottomLeft -> DIAGONAL_135
                ShimmerDirection.FromBottomLeftToTopRight -> DIAGONAL_NEGATIVE_45
                ShimmerDirection.FromBottomRightToTopLeft -> DIAGONAL_NEGATIVE_135
                is ShimmerDirection.Angle -> direction.degrees.toDouble()
            },
        )
    private val ndx = cos(angleRad).toFloat()
    private val ndy = sin(angleRad).toFloat()

    override fun brush(
        progress: Float,
        size: IntSize,
        theme: ShimmerTheme,
    ): Brush {
        val width = size.width.toFloat().coerceAtLeast(1f)
        val height = size.height.toFloat().coerceAtLeast(1f)

        val travelDistance = hypot(width, height) + shimmerWidth * 2
        val offset = -shimmerWidth + travelDistance * progress

        val start =
            Offset(
                x = width / 2f - (travelDistance / 2f) * ndx + offset * ndx,
                y = height / 2f - (travelDistance / 2f) * ndy + offset * ndy,
            )
        val end = Offset(start.x + shimmerWidth * ndx, start.y + shimmerWidth * ndy)

        return Brush.linearGradient(
            colors =
                listOf(
                    theme.baseColor.copy(alpha = theme.opacity),
                    theme.highlightColor,
                    theme.baseColor.copy(alpha = theme.opacity),
                ),
            start = start,
            end = end,
        )
    }

    private companion object {
        private const val SHIMMER_WIDTH = 200f

        private const val DIAGONAL_45 = 45.0
        private const val DIAGONAL_135 = 135.0
        private const val DIAGONAL_NEGATIVE_45 = -45.0
        private const val DIAGONAL_NEGATIVE_135 = -135.0
    }
}
