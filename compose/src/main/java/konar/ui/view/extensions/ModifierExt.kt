package konar.ui.view.extensions

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.IntSize
import konar.ui.view.theme.LocalShimmerTheme
import konar.ui.view.theme.ShimmerTheme

@Suppress("ktlint")
@Composable
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

    override fun ContentDrawScope.draw() {
        drawContent()

        if (!shimmer || size.width == 0f || size.height == 0f) {
            drawRect(Color.Transparent)
            return
        }

        val intSize = IntSize(size.width.toInt(), size.height.toInt())
        val brush = theme.effect.brush(progress, intSize, theme)
        drawRect(brush)
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        size = coordinates.size
    }
}

@Suppress("ktlint")
@Composable
fun Modifier.shimmerOrBackground(
    loaded: Boolean,
    color: Color,
): Modifier = if (loaded) this.background(color) else this.shimmerEffect()
