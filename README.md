# Shimmer Compose Library

This library provides a set of Jetpack Compose components that display a shimmer effect while content is loading. These components are designed to be easy to use and customize.

## Import library

To use the library, add the following dependency:
```
implementation("dev.skonar:shimmer-compose:0.1.2")
```

## Components

The library includes the following main components:

- `BoxShimmer`: A container that shows a shimmer effect and then reveals its content.
- `ShimmerText`: A component for displaying shimmering text placeholders.
- `ImageShimmer`: A component for displaying a shimmer effect while a local image is loading.
- `ImageShimmerNetwork`: A component for displaying a shimmer effect while a network image is loading.
- `Modifier.shimmerEffect()`: A modifier to apply a shimmer effect to any composable.

## Usage

Here are some examples of how to use the components in your project:

## Theming

The shimmer effect can be customized using `ShimmerThemeProvider`. You can provide a `ShimmerTheme` to control the colors and the effect itself. It's recommended to wrap your content with `ShimmerThemeProvider` inside your app's theme.

### ShimmerThemeProvider with MaterialTheme

Here's an example of how to integrate `ShimmerThemeProvider` with `MaterialTheme`:

```kotlin
@Composable
fun ExampleAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val shimmerTheme =
        ShimmerTheme(
            effect = LinearShimmerEffect(),
            baseColor = colorScheme.surface,
            highlightColor = colorScheme.surfaceVariant,
        )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        ShimmerThemeProvider(theme = shimmerTheme) {
            content()
        }
    }
}
```

## Predefined Effects

The library comes with a set of predefined shimmer effects that you can use out of the box.

- **`LinearShimmerEffect`**: Creates a linear gradient shimmer that moves across the composable.
- **`RadialShimmerEffect`**: Creates a radial gradient shimmer that emanates from the center.

You can specify which effect to use within the `ShimmerTheme`, as shown in the `ShimmerThemeProvider` example above.

### Modifier.shimmerEffect()

Use the `shimmerEffect()` modifier to apply a shimmer animation to any composable.

**Sample Code:**

```kotlin
Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .shimmerEffect(isShimmering = true)
)
```

### BoxShimmer

Use `BoxShimmer` to display a shimmering placeholder for any type of content.

**Sample Code:**

```kotlin
BoxShimmer(
    isShimmering = true,
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    // Content to display after shimmer
    Text("Content loaded!")
}
```

### ShimmerText

Use `ShimmerText` to create shimmering text placeholders.

**Sample Code:**

```kotlin
ShimmerText(
    isShimmering = true,
    modifier = Modifier.fillMaxWidth()
)
```

### ImageShimmer

Use `ImageShimmer` to display a shimmer effect for local images.

**Sample Code:**

```kotlin
ImageShimmer(
    isShimmering = true,
    painter = painterResource(id = R.drawable.placeholder_image),
    contentDescription = "Placeholder Image",
    modifier = Modifier.size(128.dp)
)
```

### ImageShimmerNetwork

Use `ImageShimmerNetwork` to display a shimmer effect for images loaded from a URL.

**Sample Code:**

```kotlin
ImageShimmerNetwork(
    isShimmering = true,
    imageUrl = "https://your-image-url.com/image.png",
    contentDescription = "Network Image",
    modifier = Modifier.size(128.dp)
)
```