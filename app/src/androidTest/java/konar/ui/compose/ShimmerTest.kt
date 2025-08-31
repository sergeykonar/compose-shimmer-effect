package konar.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import konar.ui.view.ui.TextShimmer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ShimmerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shimmerTextTest() {
        val textState = mutableStateOf<String?>(null)

        composeTestRule.setContent {
            TextShimmer(
                text = textState.value,
                modifier = Modifier.fillMaxWidth(),
                loadedColor = Color.Black,
            )
        }

        // Initially: shimmer is shown, text is empty
        composeTestRule.onNode(hasTestTag("shimmer")).assertExists()
        composeTestRule.onNodeWithText("").assertExists()

        // Simulate loading completion

        composeTestRule.runOnIdle {
            textState.value = "Loaded Text"
        }

        // Verify shimmer disappeared
        composeTestRule.onNode(hasTestTag("shimmer")).assertDoesNotExist()
        composeTestRule.onNodeWithText("Loaded Text").assertExists()
    }
}
