package konar.ui.view.state

sealed class ShimmerDirection {
    /** Move from top-left corner toward bottom-right corner. */
    object FromTopLeftToBottomRight : ShimmerDirection()

    /** Move from top-right corner toward bottom-left corner. */
    object FromTopRightToBottomLeft : ShimmerDirection()

    /** Move from bottom-left corner toward top-right corner. */
    object FromBottomLeftToTopRight : ShimmerDirection()

    /** Move from bottom-right corner toward top-left corner. */
    object FromBottomRightToTopLeft : ShimmerDirection()

    /** Move in a custom angle (degrees: 0° = left→right, 90° = top→bottom). */
    data class Angle(
        val degrees: Float,
    ) : ShimmerDirection()
}
