package konar.ui.view.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import konar.ui.view.extensions.shimmerEffect
import konar.ui.view.extensions.shimmerOrBackground
import konar.ui.view.state.State
import konar.ui.view.state.toDomainSate
import konar.ui.view.ui.DefaultImageShimmerValues.BackgroundColor

/**
 * A composable that displays a shimmering loading effect until an image is loaded.
 *
 * @param painter The [Painter] to display when the image is loaded.
 * @param imageSize The size of the image.
 * @param isLoaded A boolean indicating whether the image has been loaded.
 * @param modifier The [Modifier] to be applied to the composable.
 * @param contentDescription The content description for the image.
 * @param backgroundColor The background color to display when not shimmering.
 * @param shimmerShape The shape of the shimmer effect and the image.
 */
@Composable
fun ImageShimmer(
    painter: Painter?,
    imageSize: DpSize,
    isLoaded: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    backgroundColor: Color = BackgroundColor,
    shimmerShape: Shape = RoundedCornerShape(8.dp),
) {
    Box(
        modifier =
            modifier
                .size(imageSize.width, imageSize.height)
                .clip(shimmerShape)
                .shimmerOrBackground(isLoaded, backgroundColor),
    ) {
        if (isLoaded && painter != null) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}

/**
 * A composable that displays a shimmering loading effect while loading an image from a URL.
 * It uses Coil's [SubcomposeAsyncImage] to handle image loading.
 *
 * @param url The URL of the image to load.
 * @param imageSize The size of the image.
 * @param modifier The [Modifier] to be applied to the composable.
 * @param contentDescription The content description for the image.
 * @param shimmerShape The shape of the shimmer effect and the image.
 * @param contentScale The scaling algorithm to apply to the image.
 * @param enableMemoryCache Whether to enable memory caching for the image request.
 * @param enableDiskCache Whether to enable disk caching for the image request.
 * @param onStateChange A callback that is invoked when the image loading state changes.
 */
@Composable
fun ImageShimmerNetwork(
    url: String?,
    imageSize: DpSize,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shimmerShape: Shape = RoundedCornerShape(0.dp),
    contentScale: ContentScale = ContentScale.Crop,
    enableMemoryCache: Boolean = true,
    enableDiskCache: Boolean = true,
    onStateChange: ((State) -> Unit)? = null,
) {
    SubcomposeAsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .memoryCachePolicy(
                    if (enableMemoryCache) CachePolicy.ENABLED else CachePolicy.DISABLED,
                ).diskCachePolicy(
                    if (enableDiskCache) CachePolicy.ENABLED else CachePolicy.DISABLED,
                ).build(),
        contentDescription = contentDescription,
        modifier =
            modifier
                .size(imageSize.width, imageSize.height)
                .clip(shimmerShape),
        contentScale = contentScale,
    ) {
        val mappedState: State = painter.state.toDomainSate()

        LaunchedEffect(mappedState) {
            onStateChange?.invoke(mappedState)
            when (mappedState) {
                is State.Success -> {
                    Log.d(TAG, "Success loading image: $url")
                }
                is State.Error -> {
                    Log.e(TAG, "Error loading image: ${mappedState.result.throwable}")
                }
                is State.Empty, is State.Loading -> {
                    Log.d(TAG, "State: $mappedState Loading image: $url")
                }
            }
        }

        when (mappedState) {
            is State.Success -> {
                SubcomposeAsyncImageContent(modifier = Modifier)
            }
            is State.Empty, is State.Loading, is State.Error -> {
                Box(
                    modifier =
                        Modifier
                            .matchParentSize()
                            .shimmerEffect(),
                )
            }
        }
    }
}

private const val TAG = "ImageShimmerNetwork"

internal object DefaultImageShimmerValues {
    val BackgroundColor: Color = Color.Transparent
}
