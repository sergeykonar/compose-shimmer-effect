package konar.ui.view.state

import androidx.compose.ui.graphics.painter.Painter
import coil.compose.AsyncImagePainter
import coil.request.ErrorResult
import coil.request.SuccessResult

sealed class State {
    /** The current painter being drawn (may be null). */
    abstract val painter: Painter?

    /** The request has not been started. */
    data object Empty : State() {
        override val painter: Painter? get() = null
    }

    /** The request is in-progress. */
    data class Loading(
        override val painter: Painter? = null,
    ) : State()

    /** The request was successful. */
    data class Success(
        override val painter: Painter,
        val result: SuccessResult,
    ) : State()

    /** The request failed due to an exception. */
    data class Error(
        override val painter: Painter?,
        val result: ErrorResult,
    ) : State()
}

internal fun AsyncImagePainter.State.toDomainSate(): State =
    when (this) {
        is AsyncImagePainter.State.Empty -> State.Empty
        is AsyncImagePainter.State.Loading -> {
            State.Loading(this.painter)
        }
        is AsyncImagePainter.State.Success -> {
            State.Success(
                this.painter,
                this.result,
            )
        }
        is AsyncImagePainter.State.Error ->
            State.Error(
                this.painter,
                this.result,
            )
    }
