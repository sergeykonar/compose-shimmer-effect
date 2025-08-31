package konar.ui.view.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import konar.ui.view.extensions.shimmerOrBackground

/**
 * A composable that displays a shimmer effect while waiting for text to be loaded.
 * When the text is not null, it displays the text with the specified styling.
 *
 * @param text The text to be displayed. If null, the shimmer effect is shown.
 * @param modifier The modifier to be applied to the shimmer container.
 * @param textModifier The modifier to be applied to the text.
 * @param color The color of the text.
 * @param fontSize The size of the font.
 * @param fontStyle The style of the font.
 * @param fontWeight The weight of the font.
 * @param fontFamily The font family.
 * @param letterSpacing The spacing between letters.
 * @param textDecoration The decoration of the text.
 * @param textAlign The alignment of the text.
 * @param lineHeight The height of each line.
 * @param overflow How the text should be handled when it overflows.
 * @param softWrap Whether the text should be soft-wrapped.
 * @param maxLines The maximum number of lines.
 * @param minLines The minimum number of lines.
 * @param onTextLayout A callback that is executed when the text layout is calculated.
 * @param style The style of the text.
 * @param shape The shape of the shimmer.
 * @param loadedColor The background color when the text is loaded.
 */
@Composable
fun ShimmerText(
    text: String?,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    isShimmering: Boolean = false,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current,
    shape: Shape = RoundedCornerShape(6.dp),
    loadedColor: Color = Color.Transparent,
) {
    Box(
        modifier =
            modifier
                .clip(shape)
                .shimmerOrBackground(loaded = text != null && !isShimmering, loadedColor),
    ) {
        Text(
            text = if (isShimmering) "" else text.orEmpty(),
            modifier = textModifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style,
        )
    }
}
