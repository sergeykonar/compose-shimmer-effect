package konar.ui.view.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import konar.ui.view.extensions.shimmerOrBackground

/**
 * A composable that displays a shimmer effect or its content based on the `isShimmering` flag.
 *
 * @param isShimmering A boolean flag to control the shimmer effect.
 * If true, the shimmer is displayed, otherwise the content is displayed.
 * @param modifier The modifier to be applied to the layout.
 * @param backgroundColor The background color of the box when it's not shimmering.
 * @param content The content to be displayed when not shimmering.
 */
@Composable
fun BoxShimmer(
    isShimmering: Boolean,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    backgroundColor: Color = Color.Transparent,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .shimmerOrBackground(!isShimmering, backgroundColor),
        contentAlignment = contentAlignment,
    ) {
        if (!isShimmering) {
            content()
        }
    }
}
