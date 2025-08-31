package konar.ui.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import konar.ui.compose.api.RetrofitClient
import konar.ui.compose.data.RoverPhoto
import konar.ui.compose.repository.MartianPhotosApi
import konar.ui.compose.ui.theme.ExampleAppTheme
import konar.ui.compose.viewmodel.MainViewModel
import konar.ui.compose.viewmodel.MainViewModelFactory
import konar.ui.view.extensions.shimmerEffect
import konar.ui.view.state.State
import konar.ui.view.theme.ShimmerTheme
import konar.ui.view.ui.BoxShimmer
import konar.ui.view.ui.ImageShimmerNetwork
import konar.ui.view.ui.ShimmerText
import kotlinx.coroutines.delay

const val LOADING_DELAY = 5000L
const val TEXT_WIDTH_FRACTION = 0.75f

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(RetrofitClient.instance.create(MartianPhotosApi::class.java))
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExampleAppTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val photos by viewModel.photos.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentSol by viewModel.currentSol.collectAsState()

    var showContent1 by remember { mutableStateOf(true) }
    var showContent2 by remember { mutableStateOf(false) }

    fun onPrevSol() = viewModel.decrementSol()

    fun onNextSol() = viewModel.incrementSol()

    fun fetchPhotosIfEmpty() {
        if (photos.isEmpty()) viewModel.fetchPhotosBySol(currentSol)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            ToggleButtons { show1, show2 ->
                showContent1 = show1
                showContent2 = show2
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = showContent1) {
                PhotosContentState(
                    photos = photos,
                    error = error,
                    currentSol = currentSol,
                    screenWidth = LocalConfiguration.current.screenWidthDp.dp,
                    onPrev = ::onPrevSol,
                    onNext = ::onNextSol,
                    fetchIfEmpty = ::fetchPhotosIfEmpty,
                )
            }

            AnimatedVisibility(visible = showContent2) {
                ShimmerExamples()
            }
        }
    }
}

@Composable
private fun PhotosContentState(
    photos: List<RoverPhoto>,
    error: String?,
    currentSol: Int,
    screenWidth: Dp,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    fetchIfEmpty: () -> Unit,
) {
    val fetchIfEmptyState by rememberUpdatedState(fetchIfEmpty)

    LaunchedEffect(Unit) {
        fetchIfEmptyState()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        SolNavigation(currentSol, onPrev, onNext)

        if (error != null) {
            Text("Error: $error", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(photos) { photo ->
                    PhotoItem(photo, screenWidth)
                }
            }
        }
    }
}

@Composable
private fun ToggleButtons(onToggle: (Boolean, Boolean) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { onToggle(true, false) }) { Text("Show Photos") }
        Button(onClick = { onToggle(false, true) }) { Text("Show Something Else") }
    }
}

@Composable
private fun SolNavigation(
    currentSol: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        Button(onClick = onPrev) { Text("Prev Sol") }
        Button(onClick = onNext) { Text("Next Sol") }
        Spacer(modifier = Modifier.width(16.dp))
        Text("Current Sol: $currentSol", modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Composable
private fun PhotoItem(
    photo: RoverPhoto,
    screenWidth: Dp,
) {
    var isShimmering by remember(photo.id) { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        ShimmerText(
            "Rover: ${photo.rover.name}, Camera: ${photo.camera.name}",
            isShimmering = isShimmering,
            modifier = Modifier.fillMaxWidth(TEXT_WIDTH_FRACTION),
        )

        Spacer(modifier = Modifier.height(4.dp))

        ImageShimmerNetwork(
            url = photo.imgSrc,
            enableDiskCache = false,
            imageSize = DpSize(screenWidth, screenWidth),
        ) {
            if (it is State.Success) isShimmering = false
        }
    }
}

@Composable
private fun ShimmerEffectBoxExample(isShimmering: Boolean) {
    Column {
        // Added Column wrapper
        Text("Using .shimmerEffect()")
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .shimmerEffect(isShimmering)
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp)),
        )
    }
}

@Composable
private fun ShimmerExamples() {
    var isShimmering by remember { mutableStateOf(true) }
    var shimmerKey by remember { mutableIntStateOf(0) }
    val opacity = remember { Animatable(0f) }

    LaunchedEffect(shimmerKey) {
        opacity.snapTo(0f)
        isShimmering = true
        delay(LOADING_DELAY)
        isShimmering = false
    }

    LaunchedEffect(shimmerKey) {
        opacity.animateTo(
            targetValue = 1f,
            animationSpec =
                tween(
                    durationMillis = 2000,
                    easing = LinearEasing,
                ),
        )
    }

    Column {
        Button(onClick = { shimmerKey++ }) { Text("Reload Shimmer") }

        ShimmerEffectBoxExample(isShimmering = isShimmering)

        Text("Sample ShimmerText below:")
        ShimmerText(
            text = "Sample text",
            isShimmering = isShimmering,
            modifier = Modifier.size(width = 100.dp, height = 20.dp),
        )

        BoxShimmer(
            isShimmering = isShimmering,
            modifier =
                Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(80.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(80.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text("Sample text")
        }

        val theme =
            ShimmerTheme(
                baseColor = Color.LightGray,
                highlightColor = Color.White,
                opacity = opacity.value,
            )

        Box(
            modifier =
                Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(90.dp))
                    .background(Color.Red)
                    .shimmerEffect(isShimmering, theme),
        ) {
        }
    }
}
