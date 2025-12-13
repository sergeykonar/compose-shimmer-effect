package konar.ui.view.shimmer

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.IntSize
import konar.ui.view.theme.ShimmerTheme

interface ShimmerEffect {
    val durationMillis: Int

    @Composable
    fun progress(): Float = shimmerProgress(durationMillis)

    /**
     * Creates a Brush for the current shimmer animation frame.
     *
     * @param progress - current animation progress (0f..1f or some offset).
     * @param size - size of the component being drawn.
     * @param theme - theme of the component.
     */
    fun brush(
        progress: Float,
        size: IntSize,
        theme: ShimmerTheme,
    ): Brush
}

@Composable
internal fun shimmerProgress(
    durationMillis: Int,
    start: Float = 0f,
    end: Float = 1f,
    easing: Easing = LinearEasing,
    repeatMode: RepeatMode = RepeatMode.Restart,
): Float {
    val transition = rememberInfiniteTransition(label = "shimmerProgressTransition")
    return transition
        .animateFloat(
            initialValue = start,
            targetValue = end,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = durationMillis, easing = easing),
                    repeatMode = repeatMode,
                ),
            label = "shimmerProgressAnim",
        ).value
}
