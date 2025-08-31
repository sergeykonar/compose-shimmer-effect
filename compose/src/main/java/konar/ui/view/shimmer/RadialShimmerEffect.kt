package konar.ui.view.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.IntSize
import konar.ui.view.theme.ShimmerTheme
import kotlin.math.max

class RadialShimmerEffect(
    override val durationMillis: Int = 500,
    private val centerShift: Float = 1.5f,
) : ShimmerEffect {
    @Composable
    override fun progress(): Float {
        val transition = rememberInfiniteTransition(label = "shimmerTransition")
        val progress by transition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "shimmerAnim",
        )
        return progress
    }

    override fun brush(
        progress: Float,
        size: IntSize,
        theme: ShimmerTheme,
    ): Brush {
        val width = size.width.toFloat()
        val height = size.height.toFloat()
        val maxDim = max(width, height)

        val center = Offset(width / 2f, height / 2f)

        val startRadius = maxDim / 2f
        val endRadius = maxDim * centerShift

        val radius = (startRadius + (endRadius - startRadius) * progress).coerceAtLeast(1f)

        val colorStops =
            listOf(
                theme.highlightColor,
                theme.baseColor,
                theme.baseColor,
            )

        return Brush.radialGradient(
            colors = colorStops,
            center = center,
            radius = radius,
        )
    }
}
