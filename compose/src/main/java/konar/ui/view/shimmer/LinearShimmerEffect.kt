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
    private val tilt: Float
        get() =
            when (direction) {
                ShimmerDirection.FromTopLeftToBottomRight -> DIAGONAL_45
                ShimmerDirection.FromTopRightToBottomLeft -> DIAGONAL_135
                ShimmerDirection.FromBottomLeftToTopRight -> DIAGONAL_NEGATIVE_45
                ShimmerDirection.FromBottomRightToTopLeft -> DIAGONAL_NEGATIVE_135
                is ShimmerDirection.Angle -> direction.degrees
            }

    override fun brush(
        progress: Float,
        size: IntSize,
        theme: ShimmerTheme,
    ): Brush {
        val width = size.width.toFloat().coerceAtLeast(1f)
        val height = size.height.toFloat().coerceAtLeast(1f)

        val angleRad = Math.toRadians(tilt.toDouble())
        val dx = cos(angleRad).toFloat()
        val dy = sin(angleRad).toFloat()

        val length = hypot(dx, dy)
        val ndx = dx / length
        val ndy = dy / length

        val travelDistance = hypot(width, height) + shimmerWidth * 2
        val offset = -shimmerWidth + travelDistance * progress

        val startX = width / 2f - (travelDistance / 2f) * ndx + offset * ndx
        val startY = height / 2f - (travelDistance / 2f) * ndy + offset * ndy

        val start = Offset(startX, startY)
        val end = Offset(start.x + shimmerWidth * ndx, start.y + shimmerWidth * ndy)

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

    private companion object {
        private const val SHIMMER_WIDTH = 200f

        private const val DIAGONAL_45 = 45f
        private const val DIAGONAL_135 = 135f
        private const val DIAGONAL_NEGATIVE_45 = -45f
        private const val DIAGONAL_NEGATIVE_135 = -135f
    }
}
