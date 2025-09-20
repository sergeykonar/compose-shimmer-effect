package konar.ui.view.extensions

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.IntSize
import konar.ui.view.theme.LocalShimmerTheme
import konar.ui.view.theme.ShimmerTheme

@Composable
@Suppress("ktlint")
fun Modifier.shimmerEffect(
    shimmer: Boolean = true,
    theme: ShimmerTheme = LocalShimmerTheme.current,
): Modifier {
    val progress = if (shimmer) theme.effect.progress() else 0f
    return this.then(ShimmerElement(shimmer, progress, theme))
}

private data class ShimmerElement(
    val shimmer: Boolean,
    val progress: Float,
    val theme: ShimmerTheme,
) : ModifierNodeElement<ShimmerNode>() {
    override fun create(): ShimmerNode = ShimmerNode(shimmer, progress, theme)

    override fun update(node: ShimmerNode) {
        node.shimmer = shimmer
        node.progress = progress
        node.theme = theme
        node.invalidateCacheIfNeeded()
    }
}

private class ShimmerNode(
    var shimmer: Boolean,
    var progress: Float,
    var theme: ShimmerTheme,
) : Modifier.Node(),
    DrawModifierNode,
    GlobalPositionAwareModifierNode {
    private var size: IntSize = IntSize.Zero

    private var cachedBrush: Brush? = null
    private var lastProgress: Float = Float.NaN
    private var lastSize: IntSize = IntSize.Zero
    private var lastTheme: ShimmerTheme? = null

    override fun ContentDrawScope.draw() {
        drawContent()

        if (!shimmer || size.width == 0f || size.height == 0f) return

        getOrCreateBrush()
        cachedBrush?.let {
            drawRect(it)
        } ?: run {
            Log.e(TAG, "Brush is null")
        }
    }

    private fun getOrCreateBrush(): Brush =
        cachedBrush
            .takeIf { lastProgress == progress && lastSize == size && lastTheme == theme }
            ?: theme.effect.brush(progress, size, theme).also {
                cachedBrush = it
                lastProgress = progress
                lastSize = size
                lastTheme = theme
            }

    fun invalidateCacheIfNeeded() {
        if (lastTheme != theme) {
            cachedBrush = null
        }
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        if (size != coordinates.size) {
            size = coordinates.size
            cachedBrush = null
        }
    }

    private companion object {
        val TAG = ShimmerNode::class.simpleName
    }
}

@Suppress("ktlint")
@Composable
fun Modifier.shimmerOrBackground(
    loaded: Boolean,
    color: Color,
): Modifier = if (loaded) this.background(color) else this.shimmerEffect()
